
package com.shopsphere.util;

import java.io.Console;

public class AdminSetup {

    public static void main(String[] args) {

        Console console = System.console();

        if (console == null) {
            System.out.println(
                "Run this utility from a terminal that supports secure password input."
            );
            return;
        }

        char[] passwordChars = console.readPassword(
            "Enter a strong password for the ShopSphere admin account: "
        );

        try {
            String password = new String(passwordChars);

            if (password.length() < 12) {
                System.out.println(
                    "Use a password with at least 12 characters."
                );
                return;
            }

            System.out.println(
                "BCrypt hash: " + PasswordUtil.hashPassword(password)
            );

        } finally {
            java.util.Arrays.fill(passwordChars, '\0');
        }
    }
}