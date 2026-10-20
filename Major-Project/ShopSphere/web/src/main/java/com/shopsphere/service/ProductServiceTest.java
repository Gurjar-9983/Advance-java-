package com.shopsphere.service;

import com.shopsphere.model.Product;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductServiceTest {

    public static void main(String[] args) {
        ProductService productService = new ProductService();

        try {
            System.out.println("====================================");
            System.out.println("ShopSphere Product Service Test");

            // Test 1: Retrieve all products
            List<Product> products =
                    productService.getAllActiveProducts();

            System.out.println("\nTest 1: Get All Active Products");
            System.out.println("Products found: " + products.size());

            // Test 2: Retrieve one product
            System.out.println("\nTest 2: Get Product By ID");

            Optional<Product> product =
                    productService.getProductById(1);

            if (product.isPresent()) {
                System.out.println("Product: " + product.get().getName());
                System.out.println("Price: " + product.get().getPrice());
            } else {
                System.out.println("Product not found.");
            }

            // Test 3: Search products
            System.out.println("\nTest 3: Search Products");

            List<Product> results =
                    productService.searchProducts("Mouse");

            System.out.println("Search results: " + results.size());

            for (Product item : results) {
                System.out.println("- " + item.getName());
            }

            // Test 4: Validate an invalid product ID
            System.out.println("\nTest 4: Invalid Product ID");

            Optional<Product> invalidProduct =
                    productService.getProductById(-1);

            System.out.println(
                    "Invalid ID handled: " + invalidProduct.isEmpty()
            );

            System.out.println("\nService tests completed.");
            System.out.println("====================================");

        } catch (SQLException e) {
            System.err.println("Service test failed.");
            System.err.println("Reason: " + e.getMessage());
        }
    }
}
