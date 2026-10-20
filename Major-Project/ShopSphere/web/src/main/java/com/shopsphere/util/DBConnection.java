
package com.shopsphere.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/shopsphere";

    private DBConnection() {
        // Prevent object creation
    }

    public static Connection getConnection() throws SQLException {

        try {
            // Read database credentials from Tomcat JNDI
            Context initialContext = new InitialContext();

            Context environmentContext =
                    (Context) initialContext.lookup("java:comp/env");

            String user =
                    (String) environmentContext.lookup("dbUser");

            String password =
                    (String) environmentContext.lookup("dbPassword");

            // Validate database credentials
            if (user == null || user.isBlank()
                    || password == null || password.isBlank()) {

                throw new SQLException(
                        "ShopSphere database credentials are missing."
                );
            }

            // Explicitly load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            return DriverManager.getConnection(URL, user, password);

        } catch (NamingException e) {

            throw new SQLException(
                    "Unable to read database credentials from Tomcat JNDI.",
                    e
            );

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                    "MySQL JDBC driver class could not be loaded. "
                    + "Check the MySQL Connector/J JAR in WEB-INF/lib.",
                    e
            );
        }
    }
}