

package com.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/lifecycle")
public class LifeCycleServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet Initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
            "<h1>Servlet Life Cycle</h1>"
            + "<p>Check Tomcat console for lifecycle messages.</p>"
            + "<a href='index.html'>Home</a>"
        );
    }

    @Override
    public void destroy() {
        System.out.println("Servlet Destroyed");
    }
}
