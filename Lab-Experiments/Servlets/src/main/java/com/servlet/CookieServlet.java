

package com.servlet;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cookie")
public class CookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        String username = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("visitor".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }

        if (username == null) {
            Cookie cookie = new Cookie("visitor", "Student");
            cookie.setMaxAge(60 * 60);
            cookie.setHttpOnly(true);
            cookie.setPath(request.getContextPath().isEmpty()
                ? "/" : request.getContextPath());
            response.addCookie(cookie);
            username = "Student (new cookie created)";
        }

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
            "<h1>Cookie Example</h1>"
            + "<p>Visitor: " + Html.escape(username) + "</p>"
            + "<p>Refresh the page to read the cookie.</p>"
            + "<a href='index.html'>Home</a>"
        );
    }
}
