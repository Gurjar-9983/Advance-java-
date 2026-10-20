package com.shopsphere.controller;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern MOBILE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/register.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = trim(request.getParameter("name"));
        String email = trim(request.getParameter("email")).toLowerCase();
        String mobile = trim(request.getParameter("mobile"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (name.isEmpty() || name.length() > 100) {
            showError(request, response,
                    "Please enter a name with at most 100 characters.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches() || email.length() > 150) {
            showError(request, response,
                    "Please enter a valid email address.");
            return;
        }

        if (!mobile.isEmpty() && !MOBILE_PATTERN.matcher(mobile).matches()) {
            showError(request, response,
                    "Enter a valid 10-digit mobile number, or leave it blank.");
            return;
        }

        if (password == null || password.length() < 8
                || password.length() > 72) {
            showError(request, response,
                    "Password must contain 8 to 72 characters.");
            return;
        }

        if (confirmPassword == null
                || !password.equals(confirmPassword)) {
            showError(request, response,
                    "Passwords do not match.");
            return;
        }

        try {
            if (userDAO.emailExists(email)) {
                showError(request, response,
                        "An account with this email already exists.");
                return;
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setMobile(mobile.isEmpty() ? null : mobile);
            user.setPassword(PasswordUtil.hashPassword(password));

            userDAO.registerUser(user);

            response.sendRedirect(
                    request.getContextPath()
                    + "/login?registered=1"
            );

        } catch (SQLException e) {
            getServletContext().log("Registration failed.", e);
            showError(request, response,
                    "Registration could not be completed. Please try again.");
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private void showError(
            HttpServletRequest request,
            HttpServletResponse response,
            String message)
            throws ServletException, IOException {

        request.setAttribute("error", message);
        request.getRequestDispatcher(
                "/WEB-INF/views/auth/register.jsp"
        ).forward(request, response);
    }
}
