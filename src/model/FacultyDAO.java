package model;

/**
 * FacultyDAO.java
 * Faculty Data Access Object
 * Group 1: Gabriel Arias, John Arquette, Hiba Arshad, Richard Zheng
 * December 2024
 * ISTE 330
 * Instructor: Jim Habermas
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyDAO {
    public List<Faculty> getFacultyByInterest(int interestID) throws SQLException {
        String query = "SELECT f.* " +
                   "FROM Faculty f " +
                   "JOIN Faculty_Interest fi ON f.facultyID = fi.facultyID " +
                   "JOIN Interest i ON fi.interestID = i.ID " +
                   "WHERE i.ID = ?";

        List<Faculty> facultyList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, interestID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Faculty faculty = new Faculty(
                    rs.getInt("facultyID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("building"),
                    rs.getString("office")
                );
                facultyList.add(faculty);
            }
        }
        return facultyList;
    }

    public List<Faculty> getFacultyByAbstract(int abstractID) throws SQLException {
        String query = "SELECT f.* " +
                   "FROM Faculty f " +
                   "JOIN Faculty_Abstract fa ON f.facultyID = fa.facultyID " +
                   "JOIN Abstract a ON fa.abstractID = a.abstractID " +
                   "WHERE a.abstractID = ?";

        List<Faculty> facultyList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, abstractID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Faculty faculty = new Faculty(
                    rs.getInt("facultyID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("building"),
                    rs.getString("office")
                );
                facultyList.add(faculty);
            }
        }
        return facultyList;
    }

    public int getFacultyIDByName(String firstName, String lastName) throws SQLException {
        String query = "SELECT facultyID FROM Faculty WHERE firstName = ? AND lastName = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("facultyID");
            }
        }
        return -1; // Return -1 if no faculty member is found
    }
    
}