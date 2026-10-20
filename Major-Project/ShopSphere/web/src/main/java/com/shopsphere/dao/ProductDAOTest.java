package com.shopsphere.dao;

import com.shopsphere.model.Product;
import java.sql.SQLException;
import java.util.List;

public class ProductDAOTest {

    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();

        try {
            List<Product> products = productDAO.findAllActive();

            System.out.println("==================================");
            System.out.println("ShopSphere Product DAO Test");
            System.out.println("Products found: " + products.size());

            for (Product product : products) {
                System.out.println(
                    "ID: " + product.getProductId()
                    + " | Name: " + product.getName()
                    + " | Price: " + product.getPrice()
                    + " | Stock: " + product.getStock()
                );
            }

            System.out.println("==================================");

        } catch (SQLException e) {
            System.err.println("Product DAO test failed.");
            System.err.println("Reason: " + e.getMessage());
        }
    }
}
