

package com.servlet;

import java.time.Instant;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute(
            "appStartTime", Instant.now().toString()
        );
        System.out.println("Application Started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Application Stopped");
    }
}
