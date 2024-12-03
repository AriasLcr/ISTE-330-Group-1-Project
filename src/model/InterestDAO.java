package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterestDAO {
    public List<Interest> getInterestsByStudent(int studentID) throws SQLException {
        String query = "SELECT i.* FROM Interest i " +
                       "JOIN Student_Interest si ON i.ID = si.interestID " +
                       "WHERE si.studentID = ?";

        List<Interest> interestList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, studentID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Interest interest = new Interest(
                    rs.getInt("ID"),
                    rs.getString("name"),
                    rs.getString("interestDescription")
                );
                interestList.add(interest);
            }
        }
        return interestList;
    }
    
    public int saveInterest(Interest interest) throws SQLException {
        String query = "INSERT INTO Interest (name, interestDescription) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, interest.getName());
            ps.setString(2, interest.getDescription());
            ps.executeUpdate();

            // Retrieve the generated ID
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated interest ID
            }
        }
        return -1; // Return -1 if the insertion failed
    }

    public void linkInterestToStudent(int studentID, int interestID) throws SQLException {
        String query = "INSERT INTO Student_Interest (studentID, interestID) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, studentID);
            ps.setInt(2, interestID);
            ps.executeUpdate();
        }
    }

    public void linkInterestToFaculty(int facultyID, int interestID) throws SQLException {
        String query = "INSERT INTO Faculty_Interest (facultyID, interestID) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, facultyID);
            ps.setInt(2, interestID);
            ps.executeUpdate();
            System.out.println("Successfully linked faculty ID " + facultyID + " to interest ID " + interestID);
        }
    }
    
    public List<Interest> getInterestsByFaculty(int facultyID) throws SQLException {
        String query = "SELECT i.* FROM Interest i JOIN Faculty_Interest fi ON i.ID = fi.interestID WHERE fi.facultyID = ?";
        List<Interest> interests = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, facultyID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                interests.add(new Interest(
                        rs.getInt("ID"),
                        rs.getString("name"),
                        rs.getString("interestDescription")
                ));
            }
        }
        return interests;
    }
    
    public void unlinkInterestFromFaculty(int facultyID, int interestID) throws SQLException {
        String query = "DELETE FROM Faculty_Interest WHERE facultyID = ? AND interestID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, facultyID);
            ps.setInt(2, interestID);
            ps.executeUpdate();
            System.out.println("Successfully unlinked faculty ID " + facultyID + " from interest ID " + interestID);
        }
    }

    public List<Interest> getAllInterests() throws SQLException {
        String query = "SELECT * FROM Interest";
        List<Interest> interests = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                interests.add(new Interest(
                        rs.getInt("ID"),
                        rs.getString("name"),
                        rs.getString("interestDescription")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in InterestDAO.getAllInterests: " + e.getMessage());
        }
        return interests;
    }
    
}
