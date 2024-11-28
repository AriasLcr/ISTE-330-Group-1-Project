package model;

import java.sql.*;

public class AccountDAO {

    // Add a new account
    public void addAccount(String email, String password, String type) throws SQLException {
        String query = "INSERT INTO Account (email, password, type) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password); // Consider using hashed passwords for security
            ps.setString(3, type);
            ps.executeUpdate();
            System.out.println("Account added successfully for email: " + email);
        }
    }

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

    // Update account password
    public void updatePassword(String email, String newPassword) throws SQLException {
        String query = "UPDATE Account SET password = ? WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, newPassword); // Consider hashing the password
            ps.setString(2, email);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password updated successfully for email: " + email);
            } else {
                System.out.println("No account found for email: " + email);
            }
        }
    }

    // Delete an account by email
    public void deleteAccount(String email) throws SQLException {
        String query = "DELETE FROM Account WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Account deleted successfully for email: " + email);
            } else {
                System.out.println("No account found for email: " + email);
            }
        }
    }
}
