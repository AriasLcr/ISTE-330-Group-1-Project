package model;

/**
 * DatabaseConnection.java
 * Establish a connection with the database
 * Group 1: Gabriel Arias, John Arquette, Hiba Arshad, Richard Zheng
 * December 2024
 * ISTE 330
 * Instructor: Jim Habermas
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/facultyResearchDB";
    private static String user = "root"; // Default username
    private static String password; // Set dynamically

    // Set the database password at runtime
    public static void setCredentials(String dbPassword) {
        password = dbPassword;
    }

    // Establish a connection with the database
    public static Connection getConnection() throws SQLException {
        if (password == null) {
            return DriverManager.getConnection(URL, user, "student"); // Default password
        }
        return DriverManager.getConnection(URL, user, password);
    }
}
