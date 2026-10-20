
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

@WebServlet("/address")
public class AddressServlet extends HttpServlet {

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
                SELECT address_id, address_line, city, state, pincode, address_type
                FROM addresses
                WHERE user_id = ?
                ORDER BY address_id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                request.setAttribute("addresses", resultSet);
                
                // Copy rows before closing the database resources.
                java.util.List<java.util.Map<String, Object>> addressList =
                        new java.util.ArrayList<>();

                while (resultSet.next()) {
                    java.util.Map<String, Object> address =
                            new java.util.HashMap<>();

                    address.put("addressId", resultSet.getInt("address_id"));
                    address.put("addressLine", resultSet.getString("address_line"));
                    address.put("city", resultSet.getString("city"));
                    address.put("state", resultSet.getString("state"));
                    address.put("pincode", resultSet.getString("pincode"));
                    address.put("addressType", resultSet.getString("address_type"));

                    addressList.add(address);
                }

                request.setAttribute("addresses", addressList);
            }

            request.getRequestDispatcher("/WEB-INF/views/address.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load saved addresses.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String addressLine = request.getParameter("addressLine");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String pincode = request.getParameter("pincode");
        String addressType = request.getParameter("addressType");

        if (addressLine == null || addressLine.isBlank()
                || city == null || city.isBlank()
                || state == null || state.isBlank()
                || pincode == null || !pincode.matches("\\d{6}")
                || addressType == null
                || !java.util.Set.of("HOME", "WORK", "OTHER").contains(addressType)) {

            session.setAttribute("addressError",
                    "Please enter valid address details and a 6-digit PIN code.");

            response.sendRedirect(request.getContextPath() + "/address");
            return;
        }

        String sql = """
                INSERT INTO addresses
                    (user_id, address_line, city, state, pincode, address_type)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, addressLine.trim());
            statement.setString(3, city.trim());
            statement.setString(4, state.trim());
            statement.setString(5, pincode);
            statement.setString(6, addressType);

            statement.executeUpdate();

            session.setAttribute("addressSuccess",
                    "Delivery address saved successfully.");

            response.sendRedirect(request.getContextPath() + "/address");

        } catch (SQLException e) {
            throw new ServletException("Unable to save the delivery address.", e);
        }
    }
}