package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MajorDAO {
    // Retrieve a Major by its ID
    public Major getMajorByID(int majorID) throws SQLException {
        String query = "SELECT * FROM Major WHERE majorID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, majorID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Major(
                        rs.getInt("majorID"),
                        rs.getString("name")
                );
            }
        }
        return null; // Return null if no matching major is found
    }

    // Retrieve all Majors
    public List<Major> getAllMajors() throws SQLException {
        String query = "SELECT * FROM Major";
        List<Major> majors = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                majors.add(new Major(
                        rs.getInt("majorID"),
                        rs.getString("name")
                ));
            }
        }
        return majors;
    }
}
