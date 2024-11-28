package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public List<Student> getStudentsByMajor(int majorID) throws SQLException {
        String query = "SELECT * FROM Student WHERE majorID = ?";

        List<Student> studentList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, majorID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("studentID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getInt("majorID")
                );
                studentList.add(student);
            }
        }
        return studentList;
    }
}
