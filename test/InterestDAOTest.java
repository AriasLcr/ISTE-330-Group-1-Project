package test;

import model.Interest;
import model.InterestDAO;
import java.sql.*;

public class InterestDAOTest {
    public static void main(String[] args) {
        InterestDAO interestDAO = new InterestDAO();

        try {
            // Test linking faculty to interest
            interestDAO.linkInterestToFaculty(1, 2); // Link faculty ID 1 to interest ID 2

            // Test retrieving interests by faculty
            System.out.println("Interests for Faculty ID 1:");
            for (Interest interest : interestDAO.getInterestsByFaculty(1)) {
                System.out.println("- " + interest.getName());
            }

            // Test unlinking faculty from interest
            interestDAO.unlinkInterestFromFaculty(1, 2); // Unlink faculty ID 1 from interest ID 2
            System.out.println("Unlinked interest successfully.");

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
