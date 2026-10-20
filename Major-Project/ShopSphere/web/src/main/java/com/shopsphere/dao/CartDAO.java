package com.shopsphere.dao;

import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public static class CartItem {
        private final int productId;
        private final String name;
        private final String brand;
        private final BigDecimal price;
        private final int quantity;
        private final int stock;
        private final String imageUrl;

        public CartItem(int productId, String name, String brand,
                        BigDecimal price, int quantity,
                        int stock, String imageUrl) {
            this.productId = productId;
            this.name = name;
            this.brand = brand;
            this.price = price;
            this.quantity = quantity;
            this.stock = stock;
            this.imageUrl = imageUrl;
        }

        public int getProductId() { return productId; }
        public String getName() { return name; }
        public String getBrand() { return brand; }
        public BigDecimal getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public int getStock() { return stock; }
        public String getImageUrl() { return imageUrl; }

        public BigDecimal getSubtotal() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public void addItem(int userId, int productId, int quantity)
            throws SQLException {

        if (quantity < 1 || quantity > 100) {
            throw new IllegalArgumentException("Invalid quantity.");
        }

        String productSql =
                "SELECT stock FROM products WHERE product_id = ? "
                + "AND status = 'ACTIVE'";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                int stock;

                try (PreparedStatement ps =
                             connection.prepareStatement(productSql)) {
                    ps.setInt(1, productId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException(
                                    "Product is unavailable.");
                        }
                        stock = rs.getInt("stock");
                    }
                }

                String cartSql =
                        "SELECT quantity FROM cart "
                        + "WHERE user_id = ? AND product_id = ? FOR UPDATE";

                int existingQuantity = 0;

                try (PreparedStatement ps =
                             connection.prepareStatement(cartSql)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, productId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            existingQuantity = rs.getInt("quantity");
                        }
                    }
                }

                if (existingQuantity + quantity > stock) {
                    throw new IllegalArgumentException(
                            "Requested quantity exceeds available stock.");
                }

                if (existingQuantity > 0) {
                    String updateSql =
                            "UPDATE cart SET quantity = ? "
                            + "WHERE user_id = ? AND product_id = ?";

                    try (PreparedStatement ps =
                                 connection.prepareStatement(updateSql)) {
                        ps.setInt(1, existingQuantity + quantity);
                        ps.setInt(2, userId);
                        ps.setInt(3, productId);
                        ps.executeUpdate();
                    }
                } else {
                    String insertSql =
                            "INSERT INTO cart (user_id, product_id, quantity) "
                            + "VALUES (?, ?, ?)";

                    try (PreparedStatement ps =
                                 connection.prepareStatement(insertSql)) {
                        ps.setInt(1, userId);
                        ps.setInt(2, productId);
                        ps.setInt(3, quantity);
                        ps.executeUpdate();
                    }
                }

                connection.commit();

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<CartItem> getItems(int userId) throws SQLException {
        List<CartItem> items = new ArrayList<>();

        String sql = """
            SELECT p.product_id, p.name, p.brand, p.price,
                   p.stock, p.image_url, c.quantity
            FROM cart c
            JOIN products p ON p.product_id = c.product_id
            WHERE c.user_id = ?
            ORDER BY c.cart_id DESC
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new CartItem(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("brand"),
                            rs.getBigDecimal("price"),
                            rs.getInt("quantity"),
                            rs.getInt("stock"),
                            rs.getString("image_url")
                    ));
                }
            }
        }

        return items;
    }

    public void updateQuantity(int userId, int productId, int quantity)
            throws SQLException {

        if (quantity < 1 || quantity > 100) {
            throw new IllegalArgumentException("Quantity must be 1–100.");
        }

        String sql = """
            UPDATE cart c
            JOIN products p ON p.product_id = c.product_id
            SET c.quantity = ?
            WHERE c.user_id = ? AND c.product_id = ?
              AND p.status = 'ACTIVE' AND p.stock >= ?
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.setInt(4, quantity);

            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException(
                        "Cart item not found or requested quantity is unavailable.");
            }
        }
    }

    public void removeItem(int userId, int productId) throws SQLException {
        String sql =
                "DELETE FROM cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public void clearCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}
