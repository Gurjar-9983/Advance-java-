
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !"ADMIN".equals(session.getAttribute("userRole"))) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Administrator access required."
            );
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {

            request.setAttribute(
                    "productCount",
                    getCount(connection, "SELECT COUNT(*) FROM products")
            );

            request.setAttribute(
                    "customerCount",
                    getCount(connection,
                            "SELECT COUNT(*) FROM users WHERE role = 'CUSTOMER'")
            );

            request.setAttribute(
                    "orderCount",
                    getCount(connection, "SELECT COUNT(*) FROM orders")
            );

            request.setAttribute(
                    "lowStockCount",
                    getCount(connection,
                            "SELECT COUNT(*) FROM products WHERE stock <= 5 AND status = 'ACTIVE'")
            );

            List<Map<String, Object>> recentOrders = new ArrayList<>();

            String orderSql = """
                    SELECT o.order_id, u.name AS customer_name,
                           o.total_amount, o.payment_method,
                           o.payment_status, o.order_status, o.created_at
                    FROM orders o
                    JOIN users u ON o.user_id = u.user_id
                    ORDER BY o.created_at DESC, o.order_id DESC
                    LIMIT 10
                    """;

            try (PreparedStatement statement =
                         connection.prepareStatement(orderSql);
                 ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> order = new HashMap<>();

                    order.put("orderId", rs.getInt("order_id"));
                    order.put("customerName", rs.getString("customer_name"));
                    order.put("totalAmount", rs.getBigDecimal("total_amount"));
                    order.put("paymentMethod", rs.getString("payment_method"));
                    order.put("paymentStatus", rs.getString("payment_status"));
                    order.put("orderStatus", rs.getString("order_status"));
                    order.put("createdAt", rs.getTimestamp("created_at"));

                    recentOrders.add(order);
                }
            }

            List<Map<String, Object>> products = new ArrayList<>();

            String productSql = """
                    SELECT product_id, name, price, stock, status
                    FROM products
                    ORDER BY stock ASC, name ASC
                    LIMIT 10
                    """;

            try (PreparedStatement statement =
                         connection.prepareStatement(productSql);
                 ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> product = new HashMap<>();

                    product.put("productId", rs.getInt("product_id"));
                    product.put("name", rs.getString("name"));
                    product.put("price", rs.getBigDecimal("price"));
                    product.put("stock", rs.getInt("stock"));
                    product.put("status", rs.getString("status"));

                    products.add(product);
                }
            }

            request.setAttribute("recentOrders", recentOrders);
            request.setAttribute("products", products);

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/dashboard.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            throw new ServletException(
                    "Unable to load the admin dashboard.", e
            );
        }
    }

    private int getCount(Connection connection, String sql)
            throws java.sql.SQLException {

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}