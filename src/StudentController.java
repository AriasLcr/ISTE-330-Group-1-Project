/** 
 * StudentController.java
 * Group 1
 * Instructor: Jim Habermas
 * ISTE-330
 * Fall 2024
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {
    private Connection conn;

    // Constructor updated to utilize ResearchDBConnection's singleton instance
    public StudentController() {
        this.conn = ResearchDBConnection.getInstance().getConnection();
    }

    public void addStudentInterest(int studentID, String interest) throws SQLException {
        String sql = "INSERT INTO Student_Interest (studentID, interest) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentID);
            pstmt.setString(2, interest);
            pstmt.executeUpdate();
        }
    }

    public List<String> findMatchingFaculty(int studentID) throws SQLException {
        List<String> matches = new ArrayList<>();
        String sql = "SELECT f.firstName, f.lastName, f.email, f.building, f.office " +
                     "FROM Faculty f JOIN Faculty_Interest fi ON f.facultyID = fi.facultyID " +
                     "JOIN Interest i ON fi.interestID = i.ID " +
                     "JOIN Student_Interest si ON i.ID = si.interestID " +
                     "WHERE si.studentID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String match = "Faculty: " + rs.getString("firstName") + " " + rs.getString("lastName") +
                                   ", Email: " + rs.getString("email") +
                                   ", Building: " + rs.getString("building") +
                                   ", Office: " + rs.getString("office");
                    matches.add(match);
                }
            }
        }
        return matches;
    }
}
