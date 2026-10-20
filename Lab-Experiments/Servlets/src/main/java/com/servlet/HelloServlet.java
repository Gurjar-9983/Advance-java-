

package com.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("name");

        response.getWriter().println(
            "<!DOCTYPE html><html><body>"
            + "<h1>Hello Students!</h1>"
            + "<h2>Welcome to Servlet Programming</h2>"
            + (name == null || name.isBlank()
                ? "<p>Enter your name on the home page.</p>"
                : "<h3>Welcome, " + Html.escape(name) + "</h3>")
            + "<a href='index.html'>Back to Home</a>"
            + "</body></html>"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
            "<!DOCTYPE html><html><body>"
            + "<h1>POST Request Received</h1>"
            + "<h2>Welcome, " + Html.escape(name) + "</h2>"
            + "<a href='index.html'>Back to Home</a>"
            + "</body></html>"
        );
    }
}
