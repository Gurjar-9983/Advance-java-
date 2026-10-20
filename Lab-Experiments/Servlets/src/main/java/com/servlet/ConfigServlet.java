

package com.servlet;

import java.io.IOException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
    urlPatterns = "/config",
    initParams = {
        @WebInitParam(name = "college", value = "GIT Jaipur")
    }
)
public class ConfigServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        String college =
            getServletConfig().getInitParameter("college");

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
            "<h1>ServletConfig Example</h1>"
            + "<p>College: " + Html.escape(college) + "</p>"
            + "<a href='index.html'>Home</a>"
        );
    }
}
