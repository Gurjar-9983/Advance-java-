
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "CONFIRMED", "PACKED", "SHIPPED", "DELIVERED", "CANCELLED"
    );

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                && "ADMIN".equals(session.getAttribute("userRole"));
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Administrator access required.");
            return;
        }

        List<Map<String, Object>> orders = new ArrayList<>();

        String sql = """
                SELECT o.order_id, u.name AS customer_name,
                       u.email AS customer_email, o.total_amount,
                       o.payment_method, o.payment_status,
                       o.order_status, o.created_at
                FROM orders o
                JOIN users u ON u.user_id = o.user_id
                ORDER BY o.created_at DESC, o.order_id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> order = new HashMap<>();

                order.put("orderId", rs.getInt("order_id"));
                order.put("customerName", rs.getString("customer_name"));
                order.put("customerEmail", rs.getString("customer_email"));
                order.put("totalAmount", rs.getBigDecimal("total_amount"));
                order.put("paymentMethod", rs.getString("payment_method"));
                order.put("paymentStatus", rs.getString("payment_status"));
                order.put("orderStatus", rs.getString("order_status"));
                order.put("createdAt", rs.getTimestamp("created_at"));

                orders.add(order);
            }

            request.setAttribute("orders", orders);
            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/orders.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load orders.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Administrator access required.");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        int orderId;
        String status = request.getParameter("orderStatus");

        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));

            if (orderId <= 0 || status == null
                    || !ALLOWED_STATUSES.contains(status)) {
                throw new IllegalArgumentException();
            }

        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid order ID or order status.");
            return;
        }

        String sql = """
                UPDATE orders
                SET order_status = ?
                WHERE order_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, orderId);

            if (statement.executeUpdate() == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Order not found.");
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("adminOrderMessage",
                    "Order #" + orderId + " updated to " + status + ".");

            response.sendRedirect(
                    request.getContextPath() + "/admin/orders"
            );

        } catch (SQLException e) {
            throw new ServletException("Unable to update order.", e);
        }
    }
}