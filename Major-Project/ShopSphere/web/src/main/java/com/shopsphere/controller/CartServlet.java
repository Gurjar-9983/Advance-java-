package com.shopsphere.controller;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.CartDAO.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getLoggedInUserId(request);

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        try {
            List<CartItem> items = cartDAO.getItems(userId);
            BigDecimal total = BigDecimal.ZERO;

            for (CartItem item : items) {
                total = total.add(item.getSubtotal());
            }

            request.setAttribute("cartItems", items);
            request.setAttribute("cartTotal", total);

            request.getRequestDispatcher(
                    "/WEB-INF/views/cart.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            getServletContext().log("Unable to load shopping cart.", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load your cart."
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getLoggedInUserId(request);

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String productValue = request.getParameter("productId");

        try {
            if (productValue == null) {
                throw new IllegalArgumentException("Product is required.");
            }

            int productId = Integer.parseInt(productValue);

            if (productId <= 0) {
                throw new IllegalArgumentException("Invalid product.");
            }

            if ("add".equals(action)) {
                String quantityValue = request.getParameter("quantity");
                int quantity = quantityValue == null
                        || quantityValue.isBlank()
                        ? 1 : Integer.parseInt(quantityValue);

                cartDAO.addItem(userId, productId, quantity);
                response.sendRedirect(
                        request.getContextPath() + "/cart?added=1"
                );

            } else if ("update".equals(action)) {
                int quantity = Integer.parseInt(
                        request.getParameter("quantity")
                );

                cartDAO.updateQuantity(userId, productId, quantity);
                response.sendRedirect(
                        request.getContextPath() + "/cart?updated=1"
                );

            } else if ("remove".equals(action)) {
                cartDAO.removeItem(userId, productId);
                response.sendRedirect(
                        request.getContextPath() + "/cart?removed=1"
                );

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Unknown cart action."
                );
            }

        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute(
                    "cartError",
                    "Please check the product and quantity, then try again."
            );
            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (SQLException e) {
            getServletContext().log("Cart operation failed.", e);
            request.getSession().setAttribute(
                    "cartError",
                    "Your cart could not be updated. Please try again."
            );
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private Integer getLoggedInUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value = session.getAttribute("userId");

        if (value instanceof Integer) {
            return (Integer) value;
        }

        return null;
    }
}
