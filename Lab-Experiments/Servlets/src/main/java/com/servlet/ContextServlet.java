

package com.servlet;

import java.io.IOException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/context")
public class ContextServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        ServletContext context = getServletContext();
        Object startTime = context.getAttribute("appStartTime");

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
            "<h1>ServletContext Example</h1>"
            + "<p>Application: "
            + Html.escape(context.getContextPath()) + "</p>"
            + "<p>Application started: "
            + Html.escape(String.valueOf(startTime)) + "</p>"
            + "<a href='index.html'>Home</a>"
        );
    }
}
