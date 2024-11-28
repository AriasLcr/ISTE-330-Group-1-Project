package test;

import model.MajorDAO;
import model.Major;

import java.sql.SQLException;

public class MajorDAOTest {
    public static void main(String[] args) {
        try {
            MajorDAO majorDAO = new MajorDAO();

            // Test getMajorByID
            Major major = majorDAO.getMajorByID(1); // Assume there's a major with ID 1
            if (major != null) {
                System.out.println("Major Retrieved: " + major.getName());
            } else {
                System.out.println("No major found.");
            }

            // Test getAllMajors
            System.out.println("All Majors:");
            for (Major m : majorDAO.getAllMajors()) {
                System.out.println("- " + m.getName());
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}