package controller;

import model.*;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private FacultyDAO facultyDAO;
    private StudentDAO studentDAO;
    private AbstractDAO abstractDAO;
    private InterestDAO interestDAO;
    private AccountDAO accountDAO;
    private Scanner scanner;

    public Controller() {
        facultyDAO = new FacultyDAO();
        studentDAO = new StudentDAO();
        abstractDAO = new AbstractDAO();
        interestDAO = new InterestDAO();
        accountDAO = new AccountDAO();
        scanner = new Scanner(System.in);
    }

    // Entry point
    public void start() {
        System.out.print("Enter MySQL password: ");
        String password = scanner.nextLine();

        try {
            // Set database credentials
            DatabaseConnection.setCredentials(password); // Assuming "root" as the username; modify as needed

            // Test the connection
            try (Connection connection = DatabaseConnection.getConnection()) {
                System.out.println("Database connection successful!");
            }

            // Process abstracts from the directory at startup
            System.out.println("Processing abstracts from the directory...");
            String abstractsFolderPath = "./abstracts/"; // assuming you are running from src folder
            int defaultFacultyID = 1; // Replace this with dynamic faculty ID input or a configuration
            AbstractFileProcessor.processDirectory(abstractsFolderPath, abstractDAO, defaultFacultyID);

            // Continue with the program
            while (true) {
                printHeader("Main Menu");
                System.out.println("1. Login");
                System.out.println("2. View Abstracts (Public)");
                System.out.println("3. Exit");
                int choice = getUserChoice(1, 3);

                switch (choice) {
                    case 1:
                        try {
                            handleLogin();
                        } catch (SQLException e) {
                            System.out.println("Error during login: " + e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            viewAbstracts();
                        } catch (SQLException e) {
                            System.out.println("Error while fetching abstracts: " + e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        return; // Exit the program
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database. Please check your credentials and try again.");
        } catch (Exception e) {
            System.out.println("An error occurred while processing abstracts: " + e.getMessage());
        }
    }

    

    // Handles user login and directs to specific workflows
    private void handleLogin() throws SQLException {
        printHeader("Login");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (accountDAO.validateLogin(email, password)) {
            Account account = accountDAO.getAccountByEmail(email);
            System.out.println("Login successful! Welcome, " + account.getType());

            if (account.getType().equalsIgnoreCase("Faculty")) {
                handleFaculty(account);
            } else if (account.getType().equalsIgnoreCase("Student")) {
                handleStudent(account);
            }
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    // Faculty-specific workflow
    private void handleFaculty(Account facultyAccount) throws SQLException {
        int facultyID = getFacultyID(facultyAccount);
        while (true) {
            printHeader("Faculty Menu");
            System.out.println("1. Upload an Abstract");
            System.out.println("2. View Your Abstracts");
            System.out.println("3. Manage Interests");
            System.out.println("4. Back to Main Menu");
            int choice = getUserChoice(1, 4);

            switch (choice) {
                case 1:
                    uploadAbstract(facultyID);
                    break;
                case 2:
                    viewFacultyAbstracts(facultyID);
                    break;
                case 3:
                    handleFacultyInterests(facultyID);
                    break;
                case 4:
                    return;
            }
        }
    }

    // Handles abstract uploads for faculty
    private void uploadAbstract(int facultyID) throws SQLException {
        printHeader("Upload Abstract");
        System.out.print("Enter the title of the abstract: ");
        String title = scanner.nextLine();
        System.out.println("Enter the abstract content: ");
        String content = scanner.nextLine();

        Abstract newAbstract = new Abstract(0, title, content);
        int abstractID = abstractDAO.saveAbstract(newAbstract);
        abstractDAO.linkAbstractToFaculty(facultyID, abstractID);
        System.out.println("Abstract uploaded successfully!");
    }

    private void viewFacultyAbstracts(int facultyID) throws SQLException {
        System.out.println("Fetching abstracts for faculty ID " + facultyID + "...");
        var abstracts = abstractDAO.getAbstractsByFaculty(facultyID);

        if (abstracts.isEmpty()) {
            System.out.println("No abstracts found for faculty ID " + facultyID);
        } else {
            for (Abstract a : abstracts) {
                System.out.println("Title: " + a.getTitle());
                System.out.println("Content: " + a.getAbstractFile());
                System.out.println("---------------------------------");
            }
        }
    }

    private void handleFacultyInterests(int facultyID) throws SQLException {
        System.out.println("Would you like to (1) Add an Interest or (2) View Interests?");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer
    
        InterestDAO interestDAO = new InterestDAO();
    
        if (choice == 1) {
            System.out.println("Enter the interest ID to link:");
            int interestID = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer
    
            interestDAO.linkInterestToFaculty(facultyID, interestID);
            System.out.println("Interest linked successfully.");
        } else if (choice == 2) {
            System.out.println("Interests for Faculty ID " + facultyID + ":");
            for (Interest interest : interestDAO.getInterestsByFaculty(facultyID)) {
                System.out.println("- " + interest.getName());
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }
    

    // Student-specific workflow
    private void handleStudent(Account studentAccount) throws SQLException {
        int studentID = getStudentID(studentAccount);
        while (true) {
            printHeader("Student Menu");
            System.out.println("1. Input Research Topics");
            System.out.println("2. View Matched Faculty");
            System.out.println("3. Back to Main Menu");
            int choice = getUserChoice(1, 3);

            switch (choice) {
                case 1:
                    inputResearchTopics(studentID);
                    break;
                case 2:
                    viewMatchedFaculty(studentID);
                    break;
                case 3:
                    return;
            }
        }
    }

    // Add research topics for students
    private void inputResearchTopics(int studentID) throws SQLException {
        printHeader("Input Research Topics");
        System.out.println("Enter your research topics (comma-separated): ");
        String topics = scanner.nextLine();
        String[] topicArray = topics.split(",");

        for (String topic : topicArray) {
            Interest interest = new Interest(0, topic.trim(), "");
            int interestID = interestDAO.saveInterest(interest);
            interestDAO.linkInterestToStudent(studentID, interestID);
        }
        System.out.println("Research topics saved!");
    }

    // View faculty matched to student's interests
    private void viewMatchedFaculty(int studentID) throws SQLException {
        printHeader("Matched Faculty");
        List<Interest> interests = interestDAO.getInterestsByStudent(studentID);

        for (Interest i : interests) {
            List<Faculty> matchedFaculty = facultyDAO.getFacultyByInterest(i.getInterestID());
            System.out.println("Interest: " + i.getName());
            for (Faculty f : matchedFaculty) {
                System.out.println("- " + f.getFirstName() + " " + f.getLastName() + " (" + f.getEmail() + ")");
            }
        }
    }

    // Public view of all abstracts
    private void viewAbstracts() throws SQLException {
        printHeader("Public Abstracts");
        List<AbstractInfo> abstracts = abstractDAO.getAbstractsInfo();
        for (AbstractInfo info : abstracts) {
            System.out.println("Title: " + info.getTitle());
            System.out.println("Authors: " + info.getAuthors());
            System.out.println("Abstract (truncated): " + info.getTruncatedContent());
            System.out.println("---------------------------------------");
        }
    }

    private int getFacultyID(Account account) {
        String query = "SELECT facultyID FROM Faculty WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, account.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("facultyID");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching faculty ID: " + e.getMessage());
        }
        return -1; // Return -1 if no matching faculty ID is found
    }

    private int getStudentID(Account account) {
        String query = "SELECT studentID FROM Student WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, account.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("studentID");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student ID: " + e.getMessage());
        }
        return -1; // Return -1 if no matching student ID is found
    }

    // Utility: Print a header
    private void printHeader(String title) {
        System.out.println("=========================================");
        System.out.println(title);
        System.out.println("=========================================");
    }

    // Utility: Get user choice
    private int getUserChoice(int min, int max) {
        while (true) {
            try {
                System.out.print("Enter your choice (" + min + "-" + max + "): ");
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
