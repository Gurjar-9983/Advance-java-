package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {

    private static final String SELECT_ACTIVE_PRODUCTS =
            "SELECT product_id, category_id, name, brand, description, " +
            "price, discount, stock, image_url, status " +
            "FROM products WHERE status = 'ACTIVE' ORDER BY product_id DESC";

    public List<Product> findAllActive() throws SQLException {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_ACTIVE_PRODUCTS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(mapProduct(resultSet));
            }
        }

        return products;
    }

    public Optional<Product> findById(int productId) throws SQLException {
        String sql =
                "SELECT product_id, category_id, name, brand, description, " +
                "price, discount, stock, image_url, status " +
                "FROM products WHERE product_id = ? AND status = 'ACTIVE'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapProduct(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    public List<Product> search(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql =
                "SELECT product_id, category_id, name, brand, description, " +
                "price, discount, stock, image_url, status " +
                "FROM products " +
                "WHERE status = 'ACTIVE' AND (name LIKE ? OR brand LIKE ?) " +
                "ORDER BY product_id DESC";

        String searchTerm = "%" + (keyword == null ? "" : keyword.trim()) + "%";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, searchTerm);
            statement.setString(2, searchTerm);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }
        }

        return products;
    }

    private Product mapProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setCategoryId(resultSet.getInt("category_id"));
        product.setName(resultSet.getString("name"));
        product.setBrand(resultSet.getString("brand"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setDiscount(resultSet.getBigDecimal("discount"));
        product.setStock(resultSet.getInt("stock"));
        product.setImageUrl(resultSet.getString("image_url"));
        product.setStatus(resultSet.getString("status"));

        return product;
    }
}
