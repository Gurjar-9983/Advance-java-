package com.shopsphere.controller;

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

@WebServlet("/order-confirmation")
public class OrderConfirmationServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !(session.getAttribute("userId") instanceof Integer)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String orderIdValue = request.getParameter("id");

        try {
            int orderId = Integer.parseInt(orderIdValue);

            if (orderId <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String sql = """
                SELECT order_id, total_amount, payment_method,
                       payment_status, order_status, created_at
                FROM orders
                WHERE order_id = ? AND user_id = ?
                """;

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, orderId);
                ps.setInt(2, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND,
                                "Order not found."
                        );
                        return;
                    }

                    request.setAttribute("orderId", rs.getInt("order_id"));
                    request.setAttribute("totalAmount", rs.getBigDecimal("total_amount"));
                    request.setAttribute("paymentMethod", rs.getString("payment_method"));
                    request.setAttribute("paymentStatus", rs.getString("payment_status"));
                    request.setAttribute("orderStatus", rs.getString("order_status"));
                    request.setAttribute("createdAt", rs.getTimestamp("created_at"));
                }
            }

            request.getRequestDispatcher(
                    "/WEB-INF/views/order-confirmation.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid order number."
            );
        } catch (SQLException e) {
            getServletContext().log(
                    "Unable to load order confirmation.", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load order confirmation."
            );
        }
    }
}
