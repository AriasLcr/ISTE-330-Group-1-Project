import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainView {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FacultyController facultyController = new FacultyController();
        StudentController studentController = new StudentController();

        System.out.println("Welcome to the Research Collaboration System!");
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Faculty Login");
            System.out.println("2. Add Student Interest");
            System.out.println("3. Find Matching Faculty for Student");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    Faculty faculty = facultyController.checkLogin(email, password);
                    if (faculty != null) {
                        System.out.println("Login successful! Welcome, " + faculty.getFirstName() + " " + faculty.getLastName());
                        System.out.println("Faculty Details: " + facultyController.getFacultyDetails(faculty.getFacultyID()));
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter Student ID: ");
                    int studentID = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Interest: ");
                    String interest = scanner.nextLine();

                    try {
                        studentController.addStudentInterest(studentID, interest);
                        System.out.println("Interest added successfully.");
                    } catch (Exception e) {
                        System.out.println("Error adding interest: " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter Student ID: ");
                    int studentID = scanner.nextInt();

                    try {
                        List<String> matches = studentController.findMatchingFaculty(studentID);
                        if (matches.isEmpty()) {
                            System.out.println("No matching faculty found.");
                        } else {
                            System.out.println("Matching Faculty:");
                            matches.forEach(System.out::println);
                        }
                    } catch (Exception e) {
                        System.out.println("Error finding matches: " + e.getMessage());
                    }
                }
                case 4 -> {
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
