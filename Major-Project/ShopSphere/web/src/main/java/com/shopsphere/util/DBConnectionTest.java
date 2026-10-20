package com.shopsphere.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] args) {
        try (Connection connection = DBConnection.getConnection()) {

            DatabaseMetaData metadata = connection.getMetaData();

            System.out.println("====================================");
            System.out.println("ShopSphere Database Connection OK!");
            System.out.println("Database: " + metadata.getDatabaseProductName());
            System.out.println("Version: " + metadata.getDatabaseProductVersion());
            System.out.println("URL: " + metadata.getURL());
            System.out.println("Connection valid: " + connection.isValid(5));
            System.out.println("====================================");

        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            System.err.println("Reason: " + e.getMessage());
        }
    }
}
