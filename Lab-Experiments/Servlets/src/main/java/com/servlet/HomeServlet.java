

package com.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String username =
            (String) session.getAttribute("username");

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
            "<!DOCTYPE html><html><body>"
            + "<h1>Student Home</h1>"
            + "<h2>Welcome, " + Html.escape(username) + "</h2>"
            + "<p>You have successfully logged in.</p>"
            + "<a href='" + request.getContextPath()
            + "/logout'>Logout</a>"
            + "</body></html>"
        );
    }
}
