package model;

/**
 * AccountDAO.java
 * Account Data Access Object
 * Group 1: Gabriel Arias, John Arquette, Hiba Arshad, Richard Zheng
 * December 2024
 * ISTE 330
 * Instructor: Jim Habermas
 */

import java.sql.*;

public class AccountDAO {
    // Retrieve an account by email
    public Account getAccountByEmail(String email) throws SQLException {
        String query = "SELECT * FROM Account WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("accountID"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("type")
                );
            }
        }
        return null; // Return null if no account is found
    }

    // Validate login credentials
    public boolean validateLogin(String email, String password) throws SQLException {
        String query = "SELECT * FROM Account WHERE email = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Return true if a matching account is found
        }
    }
}
