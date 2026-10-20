package com.shopsphere.controller;

import com.shopsphere.dao.CheckoutDAO;
import com.shopsphere.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CheckoutDAO checkoutDAO = new CheckoutDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String sql = """
            SELECT address_id, address_line, city, state, pincode,
                   address_type
            FROM addresses
            WHERE user_id = ?
            ORDER BY address_id DESC
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                java.util.List<java.util.Map<String, Object>> addresses =
                        new java.util.ArrayList<>();

                while (rs.next()) {
                    java.util.Map<String, Object> address =
                            new java.util.HashMap<>();

                    address.put("addressId", rs.getInt("address_id"));
                    address.put("addressLine", rs.getString("address_line"));
                    address.put("city", rs.getString("city"));
                    address.put("state", rs.getString("state"));
                    address.put("pincode", rs.getString("pincode"));
                    address.put("addressType", rs.getString("address_type"));

                    addresses.add(address);
                }

                request.setAttribute("addresses", addresses);
            }

            request.getRequestDispatcher(
                    "/WEB-INF/views/checkout.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            getServletContext().log("Unable to load checkout.", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load checkout. Please try again."
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            int addressId = Integer.parseInt(
                    request.getParameter("addressId")
            );

            String paymentMethod = request.getParameter("paymentMethod");

            if (addressId <= 0
                    || (!"COD".equals(paymentMethod)
                    && !"MOCK_ONLINE".equals(paymentMethod))) {
                throw new IllegalArgumentException(
                        "Please select a valid address and payment method.");
            }

            CheckoutDAO.CheckoutResult result =
                    checkoutDAO.placeOrder(userId, addressId, paymentMethod);

            response.sendRedirect(
                    request.getContextPath()
                    + "/order-confirmation?id=" + result.getOrderId()
            );

        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute(
                    "checkoutError",
                    "Please select a valid delivery address and payment method."
            );
            response.sendRedirect(request.getContextPath() + "/checkout");

        } catch (SQLException e) {
            getServletContext().log("Checkout failed.", e);
            request.getSession().setAttribute(
                    "checkoutError",
                    "We could not place your order. Please review your cart and try again."
            );
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }

    private Integer getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value = session.getAttribute("userId");
        return value instanceof Integer ? (Integer) value : null;
    }
}
