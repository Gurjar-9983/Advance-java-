package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> getAllActiveProducts() throws SQLException {
        return productDAO.findAllActive();
    }

    public Optional<Product> getProductById(int productId)
            throws SQLException {

        if (productId <= 0) {
            return Optional.empty();
        }

        return productDAO.findById(productId);
    }

    public List<Product> searchProducts(String keyword)
            throws SQLException {

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllActiveProducts();
        }

        return productDAO.search(keyword.trim());
    }
}
