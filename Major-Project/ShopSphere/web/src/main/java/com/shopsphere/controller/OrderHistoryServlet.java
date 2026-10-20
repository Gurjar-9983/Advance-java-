
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

@WebServlet("/orders")
public class OrderHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String sql = """
                SELECT order_id, total_amount, payment_method,
                       payment_status, order_status, created_at
                FROM orders
                WHERE user_id = ?
                ORDER BY created_at DESC, order_id DESC
                """;

        List<Map<String, Object>> orders = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> order = new HashMap<>();

                    order.put("orderId", resultSet.getInt("order_id"));
                    order.put("totalAmount", resultSet.getBigDecimal("total_amount"));
                    order.put("paymentMethod", resultSet.getString("payment_method"));
                    order.put("paymentStatus", resultSet.getString("payment_status"));
                    order.put("orderStatus", resultSet.getString("order_status"));
                    order.put("createdAt", resultSet.getTimestamp("created_at"));

                    orders.add(order);
                }
            }

            request.setAttribute("orders", orders);

            request.getRequestDispatcher("/WEB-INF/views/orders.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load order history.", e);
        }
    }
}