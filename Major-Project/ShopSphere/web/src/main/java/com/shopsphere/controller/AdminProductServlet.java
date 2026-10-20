
package com.shopsphere.controller;

import com.shopsphere.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

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

        String editId = request.getParameter("edit");

        try (Connection connection = DBConnection.getConnection()) {

            if (editId != null && !editId.isBlank()) {
                int productId;

                try {
                    productId = Integer.parseInt(editId);
                    if (productId <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid product ID.");
                    return;
                }

                String findSql = """
                        SELECT product_id, name, brand, description,
                               price, discount, stock, image_url, status
                        FROM products
                        WHERE product_id = ?
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(findSql)) {
                    statement.setInt(1, productId);

                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            Map<String, Object> product = new HashMap<>();
                            product.put("productId", rs.getInt("product_id"));
                            product.put("name", rs.getString("name"));
                            product.put("brand", rs.getString("brand"));
                            product.put("description", rs.getString("description"));
                            product.put("price", rs.getBigDecimal("price"));
                            product.put("discount", rs.getBigDecimal("discount"));
                            product.put("stock", rs.getInt("stock"));
                            product.put("imageUrl", rs.getString("image_url"));
                            product.put("status", rs.getString("status"));
                            request.setAttribute("editProduct", product);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                                    "Product not found.");
                            return;
                        }
                    }
                }
            }

            List<Map<String, Object>> products = new ArrayList<>();

            String listSql = """
                    SELECT product_id, name, brand, price, stock, status
                    FROM products
                    ORDER BY product_id DESC
                    """;

            try (PreparedStatement statement =
                         connection.prepareStatement(listSql);
                 ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productId", rs.getInt("product_id"));
                    product.put("name", rs.getString("name"));
                    product.put("brand", rs.getString("brand"));
                    product.put("price", rs.getBigDecimal("price"));
                    product.put("stock", rs.getInt("stock"));
                    product.put("status", rs.getString("status"));
                    products.add(product);
                }
            }

            request.setAttribute("products", products);
            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/products.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load products.", e);
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

        String productIdText = request.getParameter("productId");
        String name = trim(request.getParameter("name"));
        String brand = trim(request.getParameter("brand"));
        String description = trim(request.getParameter("description"));
        String priceText = trim(request.getParameter("price"));
        String discountText = trim(request.getParameter("discount"));
        String stockText = trim(request.getParameter("stock"));
        String imageUrl = trim(request.getParameter("imageUrl"));
        String status = trim(request.getParameter("status"));

        if (name == null || name.isBlank() || name.length() > 200) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Product name is required and must be at most 200 characters.");
            return;
        }

        if (brand != null && brand.length() > 100) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Brand must be at most 100 characters.");
            return;
        }

        if (imageUrl != null && imageUrl.length() > 500) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Image URL must be at most 500 characters.");
            return;
        }

        if (status == null
                || !("ACTIVE".equals(status) || "INACTIVE".equals(status))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product status.");
            return;
        }

        BigDecimal price;
        BigDecimal discount;
        int stock;

        try {
            price = new BigDecimal(priceText);
            discount = (discountText == null || discountText.isBlank())
                    ? BigDecimal.ZERO : new BigDecimal(discountText);
            stock = Integer.parseInt(stockText);

            if (price.compareTo(BigDecimal.ZERO) <= 0
                    || price.scale() > 2
                    || price.precision() > 10
                    || discount.compareTo(BigDecimal.ZERO) < 0
                    || discount.compareTo(new BigDecimal("100")) > 0
                    || discount.scale() > 2
                    || stock < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException | ArithmeticException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Enter a valid price, discount (0-100), and non-negative stock.");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {

            if (productIdText == null || productIdText.isBlank()) {

                String insertSql = """
                        INSERT INTO products
                            (name, brand, description, price, discount,
                             stock, image_url, status)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(insertSql)) {
                    statement.setString(1, name);
                    statement.setString(2, blankToNull(brand));
                    statement.setString(3, blankToNull(description));
                    statement.setBigDecimal(4, price);
                    statement.setBigDecimal(5, discount);
                    statement.setInt(6, stock);
                    statement.setString(7, blankToNull(imageUrl));
                    statement.setString(8, status);
                    statement.executeUpdate();
                }

            } else {

                int productId;

                try {
                    productId = Integer.parseInt(productIdText);
                    if (productId <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid product ID.");
                    return;
                }

                String updateSql = """
                        UPDATE products
                        SET name = ?, brand = ?, description = ?, price = ?,
                            discount = ?, stock = ?, image_url = ?, status = ?
                        WHERE product_id = ?
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(updateSql)) {
                    statement.setString(1, name);
                    statement.setString(2, blankToNull(brand));
                    statement.setString(3, blankToNull(description));
                    statement.setBigDecimal(4, price);
                    statement.setBigDecimal(5, discount);
                    statement.setInt(6, stock);
                    statement.setString(7, blankToNull(imageUrl));
                    statement.setString(8, status);
                    statement.setInt(9, productId);

                    if (statement.executeUpdate() == 0) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND,
                                "Product not found.");
                        return;
                    }
                }
            }

            HttpSession session = request.getSession();
            session.setAttribute("adminProductMessage",
                    productIdText == null || productIdText.isBlank()
                            ? "Product added successfully."
                            : "Product updated successfully.");

            response.sendRedirect(
                    request.getContextPath() + "/admin/products"
            );

        } catch (SQLException e) {
            throw new ServletException("Unable to save product.", e);
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}