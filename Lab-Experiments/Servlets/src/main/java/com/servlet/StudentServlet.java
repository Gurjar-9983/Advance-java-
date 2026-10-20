

package com.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");

        response.setContentType("text/html;charset=UTF-8");

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || course == null || course.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(
                "<h2>All fields are required.</h2>"
                + "<a href='student.html'>Try again</a>"
            );
            return;
        }

        response.getWriter().println(
            "<!DOCTYPE html><html><body>"
            + "<h1>Registration Successful</h1>"
            + "<p>Name: " + Html.escape(name) + "</p>"
            + "<p>Email: " + Html.escape(email) + "</p>"
            + "<p>Course: " + Html.escape(course) + "</p>"
            + "<a href='index.html'>Home</a>"
            + "</body></html>"
        );
    }
}
