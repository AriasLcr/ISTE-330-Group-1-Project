package controller;

/**
 * Controller.java
 * Controller class for the application
 * Group 1: Gabriel Arias, John Arquette, Hiba Arshad, Richard Zheng
 * December 2024
 * Instructor: Jim Habermas
 */

import model.*;
import view.*;
import java.sql.*;
import java.util.Scanner;
import java.util.List;

public class Controller {
    private FacultyDAO facultyDAO;
    private StudentDAO studentDAO;
    private AbstractDAO abstractDAO;
    private InterestDAO interestDAO;
    private AccountDAO accountDAO;
    private MajorDAO majorDAO;
    
    private View view;
    private Scanner scanner;

    public Controller() {
        facultyDAO = new FacultyDAO();
        studentDAO = new StudentDAO();
        abstractDAO = new AbstractDAO();
        interestDAO = new InterestDAO();
        accountDAO = new AccountDAO();
        majorDAO = new MajorDAO();
        scanner = new Scanner(System.in);

        view = new View(facultyDAO, studentDAO, abstractDAO, interestDAO, accountDAO, majorDAO, scanner);
    }

    // Entry point
    public void start() {
        System.out.print("Enter MySQL password (default: student): ");
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
            String abstractsFolderPath = "../src/abstracts/"; // This is assuming that the project is being run from the classes directory
            AbstractFileProcessor.processDirectory(abstractsFolderPath, abstractDAO, facultyDAO);

            // Continue with the program
            while (true) {
                printHeader("Main Menu");
                System.out.println("1. Login using email (Faculty/Student)");
                System.out.println("2. Login as a guest");
                System.out.println("3. View Abstracts (Public)");
                System.out.println("4. Exit");
                int choice = getUserChoice(1, 4);

                switch (choice) {
                    case 1:
                        try {
                            handleLogin();
                        } catch (SQLException e) {
                            System.out.println("Error during login: " + e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.println("Guest login successful! Welcome, Guest");
                        try {
                            handlePublicLogin();
                        } catch (Exception e) {
                            System.out.println("An error occurred: " + e.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            printHeader("Public Abstracts");
                            view.viewAbstracts();
                        } catch (SQLException e) {
                            System.out.println("Error while fetching abstracts: " + e.getMessage());
                        }
                        break;
                        
                    case 4:
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

    public String getAccountName(String email, String type) throws SQLException {
        String name = "";
        String tableName;
    
        // Validate the type and map it to the corresponding table and column
        if ("student".equalsIgnoreCase(type)) {
            tableName = "Student";
        } else if ("faculty".equalsIgnoreCase(type)) {
            tableName = "Faculty";
        } else {
            throw new IllegalArgumentException("Invalid account type: " + type);
        }
    
        String query = "SELECT s.firstName, s.lastName FROM " + tableName + " s " +
               "JOIN Account a ON s.email = a.email " +
               "WHERE a.email = ?";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                name = firstName + " " + lastName; // Combine first and last name
            }
        } catch (SQLException e) {
            System.err.println("Error fetching account name: " + e.getMessage());
        }
        return name;
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
            System.out.println("Login successful! Welcome, " + getAccountName(email, account.getType().toLowerCase()));

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
            printHeader("Faculty Menu (" + getAccountName(facultyAccount.getEmail(), facultyAccount.getType()) + ")");
            System.out.println("1. Upload an Abstract");
            System.out.println("2. View Your Abstracts");
            System.out.println("3. Delete an Abstract");
            System.out.println("4. Manage Interests");
            System.out.println("5. View Students by Major");
            System.out.println("6. View Students by Interest");
            System.out.println("7. Back to Main Menu");
            int choice = getUserChoice(1, 7);

            switch (choice) {
                case 1:
                    printHeader("Upload Abstract");
                    uploadAbstract(facultyID);
                    continue;
                case 2:
                    printHeader("View Faculty Abstracts");
                    view.viewFacultyAbstracts(facultyID);
                    continue;
                case 3:
                   printHeader("Delete an Abstract"); 
                   deleteAbstract(facultyID);
                   continue;
                case 4:
                    handleFacultyInterests(facultyID);
                    continue;
                case 5:
                    printHeader("View Students by Major");
                    view.viewStudentsByMajor();
                    continue;
                case 6:
                    printHeader("View Students by Interest");
                    view.viewStudentsByInterest();
                    continue;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
            }
        }
    }

    // Public login workflow
    private void handlePublicLogin() throws SQLException {
        while (true) {
            printHeader("Guest Menu");
            System.out.println("1. View Faculty by Interest");
            System.out.println("2. View Faculty by Abstract");
            System.out.println("3. View Students by Interest");
            System.out.println("4. Back to Main Menu");

            int choice = getUserChoice(1, 4);
            
            switch (choice) {
                case 1:
                    printHeader("View Faculty by Interest");
                    view.viewFacultyByInterest();
                    continue;
                case 2:
                    printHeader("View Faculty by Abstract");
                    view.viewFacultyByAbstract();
                    continue;
                case 3:
                    printHeader("View Students by Interest");
                    view.viewStudentsByInterest();
                    continue;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
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
    
    //handles abstract deletion
    private void deleteAbstract(int facultyID) throws SQLException {
       List<Abstract> abstracts = abstractDAO.getAbstractsByFaculty(facultyID);
       if (abstracts.isEmpty()) {
           System.out.println("No abstracts found to delete.");
           return;
       }

    System.out.println("Select an abstract to delete:");
    for (int i = 0; i < abstracts.size(); i++) {
        System.out.println((i + 1) + ". " + abstracts.get(i).getTitle());
    }

    int choice = getUserChoice(1, abstracts.size());
    Abstract selectedAbstract = abstracts.get(choice - 1);

    abstractDAO.deleteAbstract(selectedAbstract.getAbstractID());
    System.out.println("Abstract titled '" + selectedAbstract.getTitle() + "' has been deleted.");
}



    private void handleFacultyInterests(int facultyID) throws SQLException {
        printHeader("Manage Interests");
        System.out.println("Would you like to (1) Add an Interest or (2) View Interests?");
        int choice = scanner.nextInt();
        scanner.nextLine();
    
        InterestDAO interestDAO = new InterestDAO();
    
        if (choice == 1) {
            System.out.println("Enter the interest ID to link:");
            int interestID = scanner.nextInt();
            scanner.nextLine();
    
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
        String studentName = getAccountName(studentAccount.getEmail(), studentAccount.getType());
        while (true) {
            printHeader("Student Menu (" + studentName + ")");
            System.out.println("1. Input Research Topics");
            System.out.println("2. View Matched Faculty");
            System.out.println("3. View Faculty by Interest");
            System.out.println("4. View Faculty by Abstract");
            System.out.println("5. View your Interests");
            System.out.println("6. Back to Main Menu");
            int choice = getUserChoice(1, 6);
            
            switch (choice) {
                case 1:
                    printHeader("Input Research Topics");
                    inputResearchTopics(studentID);
                    continue;
                case 2:
                    printHeader("Matched Faculty");
                    view.viewMatchedFaculty(studentID);
                    continue;
                case 3:
                    printHeader("View Faculty by Interest");
                    view.viewFacultyByInterest();
                    continue;
                case 4:
                    printHeader("View Faculty by Abstract");
                    view.viewFacultyByAbstract();
                    continue;
                case 5:
                    printHeader("View your Interests " + studentName);
                    view.viewStudentInterests(studentID);
                    continue;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
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
        clearScreen();
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

    // Utility: Clear the screen
    public void clearScreen() {
        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
}
