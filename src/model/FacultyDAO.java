package model;

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
}