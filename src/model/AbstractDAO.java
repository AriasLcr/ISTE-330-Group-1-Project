package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbstractDAO {
    public List<Abstract> getAbstractsByFaculty(int facultyID) throws SQLException {
        String query = "SELECT a.* FROM Abstract a " +
                       "JOIN Faculty_Abstract fa ON a.abstractID = fa.abstractID " +
                       "WHERE fa.facultyID = ?";

        List<Abstract> abstractList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, facultyID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Abstract abstractObject = new Abstract(
                    rs.getInt("abstractID"),
                    rs.getString("title"),
                    rs.getString("abstractFile")
                );
                abstractList.add(abstractObject);
            }
        }
        return abstractList;
    }

    public int saveAbstract(Abstract abstractData) throws SQLException {
        String query = "INSERT INTO Abstract (title, abstractFile) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, abstractData.getTitle());
            ps.setString(2, abstractData.getAbstractFile());
            ps.executeUpdate();

            // Retrieve the generated ID
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated abstract ID
            }
        }
        return -1; // Return -1 if the insertion failed
    }

    public void linkAbstractToFaculty(int facultyID, int abstractID) throws SQLException {
        String query = "INSERT INTO Faculty_Abstract (facultyID, abstractID) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, facultyID);
            ps.setInt(2, abstractID);
            ps.executeUpdate();
        }
    }

    public List<AbstractInfo> getAbstractsInfo() throws SQLException {
        String query = "CALL GetAbstractsInfo()";
        List<AbstractInfo> abstracts = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement cs = connection.prepareCall(query)) {
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                abstracts.add(new AbstractInfo(
                        rs.getString("Abstract Title"),
                        rs.getString("Author(s)"),
                        rs.getString("Abstract (truncated)")
                ));
            }
        }
        return abstracts;
    }

    public List<Abstract> getAllAbstracts() throws SQLException {
        String query = "SELECT * FROM Abstract";
        List<Abstract> abstracts = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                abstracts.add(new Abstract(
                        rs.getInt("abstractID"),
                        rs.getString("title"),
                        rs.getString("abstractFile")
                ));
            }
        }
        return abstracts;
    }
    
    public boolean abstractExists(String title, String content) throws SQLException {
        String query = "SELECT COUNT(*) FROM Abstract WHERE title = ? AND abstractFile = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, title);
            ps.setString(2, content);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if at least one match is found
            }
        }
        return false;
    }
}
