package com.shopsphere.controller;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String keyword = request.getParameter("q");

        try {
            List<Product> products;

            if (keyword != null && !keyword.trim().isEmpty()) {
                products = productService.searchProducts(keyword);
            } else {
                products = productService.getAllActiveProducts();
            }

            request.setAttribute("products", products);
            request.setAttribute("searchKeyword", keyword);

            request.getRequestDispatcher("/WEB-INF/views/products.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            getServletContext().log("Unable to retrieve products", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load products. Please try again later."
            );
        }
    }
}
