package view;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import model.*;


public class View {
    private FacultyDAO facultyDAO;
    private StudentDAO studentDAO;
    private AbstractDAO abstractDAO;
    private InterestDAO interestDAO;
    private AccountDAO accountDAO;
    private MajorDAO majorDAO;
    private Scanner scanner;

    public View(FacultyDAO facultyDao, StudentDAO studentDao, AbstractDAO abstractDao, InterestDAO interestDao, AccountDAO accountDao, MajorDAO majorDao, Scanner scanner) {
        this.facultyDAO = facultyDao;
        this.studentDAO = studentDao;
        this.abstractDAO = abstractDao;
        this.interestDAO = interestDao;
        this.accountDAO = accountDao;
        this.majorDAO = majorDao;
        this.scanner = scanner;
    }

    public void viewFacultyByInterest() throws SQLException {
        System.out.println("Fetching all interests...");
        List<Interest> interests = interestDAO.getAllInterests();

        for (int i = 0; i < interests.size(); i++) {
            System.out.println((i + 1) + ". " + interests.get(i).getName());
        }

        System.out.print("Enter the interest ID to view faculty: ");
        int interestID = scanner.nextInt();
        scanner.nextLine();
        String interestName = interests.get(interestID - 1).getName();
        System.out.println("\nFetching faculty for " + interestName + "...\n");

        List<Faculty> faculty = facultyDAO.getFacultyByInterest(interestID);
        if (faculty.isEmpty()) {
            System.out.println("No faculty found for interest ID " + interestID);
        } else {
            for (Faculty f : faculty) {
                System.out.println("Name: " + f.getFirstName() + " " + f.getLastName());
                System.out.println("Email: " + f.getEmail());
                System.out.println("---------------------------------");
            }
        }
    }

    public void viewFacultyByAbstract() throws SQLException {
        System.out.println("Fetching all abstracts...");
        List<Abstract> abstracts = abstractDAO.getAllAbstracts();

        for (int i = 0; i < abstracts.size(); i++) {
            System.out.println((i + 1) + ". " + abstracts.get(i).getTitle());
        }

        System.out.print("Enter the abstract ID to view faculty: ");
        int abstractID = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nFetching faculty for abstract ID " + abstractID + "... \n");

        List<Faculty> faculty = facultyDAO.getFacultyByAbstract(abstractID);
        if (faculty.isEmpty()) {
            System.out.println("No faculty found for abstract ID " + abstractID);
        } else {
            for (Faculty f : faculty) {
                
                System.out.println("Name: " + f.getFirstName() + " " + f.getLastName());
                System.out.println("Email: " + f.getEmail());
                System.out.println("---------------------------------");
            }
        }
    }

    public void viewStudentsByMajor() throws SQLException {
        System.out.println("Fetching all majors...");
        List<Major> majors = majorDAO.getAllMajors();
        int majorID = 0;
        System.out.println("Majors:");
        for (Major m : majors) {
            System.out.println(m.getMajorID() + ". " + m.getName());
        }
        System.out.println("Enter the major ID to view students: ");
        majorID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\nFetching students for major ID " + majorID + "...\n");

        var students = studentDAO.getStudentsByMajor(majorID);
        if (students.isEmpty()) {
            System.out.println("No students found for major ID " + majorID);
        } else {
            for (Student s : students) {
                System.out.println("Name: " + s.getFirstName() + " " + s.getLastName());
                System.out.println("Email: " + s.getEmail());
                System.out.println("---------------------------------");
            }
        }
    }

    public void viewStudentsByInterest() throws SQLException {
        System.out.println("Fetching all interests...");
        List<Interest> interests = interestDAO.getAllInterests();

        for (int i = 0; i < interests.size(); i++) {
            System.out.println((i + 1) + ". " + interests.get(i).getName());
        }

        System.out.print("Enter the interest ID to view students: ");
        int interestID = scanner.nextInt();
        scanner.nextLine();
        String interestName = interests.get(interestID - 1).getName();
        System.out.println("\nFetching students for " + interestName + "...\n");
        List<Student> students = studentDAO.getStudentsByInterest(interestID);

        if (students.isEmpty()) {
            System.out.println("No students found for interest ID " + interestID);
        } else {
            for (Student s : students) {
                System.out.println("Name: " + s.getFirstName() + " " + s.getLastName());
                System.out.println("Email: " + s.getEmail());
                System.out.println("---------------------------------");
            }
        }
    }

    
    public void viewFacultyAbstracts(int facultyID) throws SQLException {
        System.out.println("\nFetching abstracts for faculty ID " + facultyID + "...\n");
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

    // View faculty matched to student's interests
    public void viewMatchedFaculty(int studentID) throws SQLException {
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
    public void viewAbstracts() throws SQLException {
        List<AbstractInfo> abstracts = abstractDAO.getAbstractsInfo();
        for (AbstractInfo info : abstracts) {
            System.out.println("Title: " + info.getTitle());
            System.out.println("Authors: " + info.getAuthors());
            System.out.println("Abstract (truncated): " + info.getTruncatedContent());
            System.out.println("---------------------------------------");
        }
    }

}
