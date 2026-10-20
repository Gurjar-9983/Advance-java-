package com.shopsphere.dao;

import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckoutDAO {

    public static class CheckoutResult {
        private final int orderId;
        private final BigDecimal total;
        private final String paymentStatus;

        public CheckoutResult(int orderId, BigDecimal total,
                              String paymentStatus) {
            this.orderId = orderId;
            this.total = total;
            this.paymentStatus = paymentStatus;
        }

        public int getOrderId() { return orderId; }
        public BigDecimal getTotal() { return total; }
        public String getPaymentStatus() { return paymentStatus; }
    }

    private static class LineItem {
        int productId;
        int quantity;
        int stock;
        BigDecimal price;

        LineItem(int productId, int quantity, int stock, BigDecimal price) {
            this.productId = productId;
            this.quantity = quantity;
            this.stock = stock;
            this.price = price;
        }
    }

    public CheckoutResult placeOrder(int userId, int addressId,
                                     String paymentMethod)
            throws SQLException {

        boolean cod = "COD".equals(paymentMethod);
        boolean mockOnline = "MOCK_ONLINE".equals(paymentMethod);

        if (!cod && !mockOnline) {
            throw new IllegalArgumentException("Invalid payment method.");
        }

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                // Confirm the address belongs to this customer.
                String addressSql =
                        "SELECT address_id FROM addresses "
                        + "WHERE address_id = ? AND user_id = ? FOR UPDATE";

                try (PreparedStatement ps =
                             connection.prepareStatement(addressSql)) {
                    ps.setInt(1, addressId);
                    ps.setInt(2, userId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException(
                                    "Please select a valid delivery address.");
                        }
                    }
                }

                // Lock the customer's cart rows while placing the order.
                String cartSql = """
                    SELECT c.product_id, c.quantity, p.stock, p.price
                    FROM cart c
                    JOIN products p ON p.product_id = c.product_id
                    WHERE c.user_id = ? AND p.status = 'ACTIVE'
                    FOR UPDATE
                    """;

                List<LineItem> items = new ArrayList<>();
                BigDecimal total = BigDecimal.ZERO;

                try (PreparedStatement ps =
                             connection.prepareStatement(cartSql)) {
                    ps.setInt(1, userId);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int quantity = rs.getInt("quantity");
                            int stock = rs.getInt("stock");

                            if (quantity < 1 || quantity > stock) {
                                throw new IllegalArgumentException(
                                        "A product in your cart has insufficient stock.");
                            }

                            LineItem item = new LineItem(
                                    rs.getInt("product_id"),
                                    quantity,
                                    stock,
                                    rs.getBigDecimal("price")
                            );

                            items.add(item);
                            total = total.add(
                                    item.price.multiply(
                                            BigDecimal.valueOf(item.quantity)
                                    )
                            );
                        }
                    }
                }

                if (items.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Your cart is empty or contains unavailable products.");
                }

                String paymentStatus = cod ? "PENDING" : "SUCCESS";
                String orderSql = """
                    INSERT INTO orders
                    (user_id, address_id, total_amount, discount,
                     payment_method, payment_status, order_status)
                    VALUES (?, ?, ?, 0.00, ?, ?, 'PLACED')
                    """;

                int orderId;

                try (PreparedStatement ps = connection.prepareStatement(
                        orderSql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, userId);
                    ps.setInt(2, addressId);
                    ps.setBigDecimal(3, total);
                    ps.setString(4, paymentMethod);
                    ps.setString(5, paymentStatus);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException(
                                    "Unable to generate order ID.");
                        }
                        orderId = keys.getInt(1);
                    }
                }

                String itemSql = """
                    INSERT INTO order_items
                    (order_id, product_id, quantity, price)
                    VALUES (?, ?, ?, ?)
                    """;

                String stockSql = """
                    UPDATE products
                    SET stock = stock - ?
                    WHERE product_id = ? AND stock >= ?
                      AND status = 'ACTIVE'
                    """;

                try (PreparedStatement itemPs =
                             connection.prepareStatement(itemSql);
                     PreparedStatement stockPs =
                             connection.prepareStatement(stockSql)) {

                    for (LineItem item : items) {
                        itemPs.setInt(1, orderId);
                        itemPs.setInt(2, item.productId);
                        itemPs.setInt(3, item.quantity);
                        itemPs.setBigDecimal(4, item.price);
                        itemPs.executeUpdate();

                        stockPs.setInt(1, item.quantity);
                        stockPs.setInt(2, item.productId);
                        stockPs.setInt(3, item.quantity);

                        if (stockPs.executeUpdate() != 1) {
                            throw new IllegalArgumentException(
                                    "Stock changed during checkout. Please retry.");
                        }
                    }
                }

                String paymentSql = """
                    INSERT INTO payments
                    (order_id, payment_method, transaction_reference,
                     amount, payment_status)
                    VALUES (?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement ps =
                             connection.prepareStatement(paymentSql)) {
                    ps.setInt(1, orderId);
                    ps.setString(2, paymentMethod);
                    ps.setString(3, mockOnline
                            ? "MOCK-" + UUID.randomUUID()
                            : null);
                    ps.setBigDecimal(4, total);
                    ps.setString(5, paymentStatus);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = connection.prepareStatement(
                        "DELETE FROM cart WHERE user_id = ?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }

                connection.commit();

                return new CheckoutResult(orderId, total, paymentStatus);

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}
