// John Arquette
// Date: 11-16-2024
// FacultyResearchDB Presentation Layer for Ingesting Abstracts

import javax.swing.*;
import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class AbstractPresentationLayer {
    private AbstractDataLayer dl;
    private Scanner scanner;

    public AbstractPresentationLayer() {
        dl = new AbstractDataLayer();
        scanner = new Scanner(System.in);

        setupDatabaseConnection();
        ingestAbstracts();
        dl.close();
    }

    private void setupDatabaseConnection() {
        String userName = "root"; // Change this as needed
        String password;

        // Prompt for password with hidden input
        JPasswordField pf = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(null, pf, "Enter Database Password (Leave blank for default 'student')", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            password = new String(pf.getPassword());
        } else {
            System.out.println("No password entered. Exiting...");
            System.exit(0);
            return; // In case System.exit() is removed
        }

        // Use default password if none is provided
        if (password.isEmpty()) {
            password = "student";
        }

        if (!dl.connect("facultyResearchDB", userName, password)) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database. Exiting...");
            System.exit(0);
        }
    }

    private void ingestAbstracts() {
        System.out.print("Enter the directory path containing abstract files: ");
        String directoryPath = scanner.nextLine();
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("No .txt files found in the specified directory.");
            return;
        }

        Map<String, Integer> facultyMap = dl.getFacultyMap();

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String title = null;
                String authors = null;
                StringBuilder abstractContent = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Title:")) {
                        title = line.replace("Title:", "").trim();
                    } else if (line.startsWith("By:")) {
                        authors = line.replace("By:", "").trim();
                    } else {
                        abstractContent.append(line).append("\n");
                    }
                }

                if (title != null && authors != null && abstractContent.length() > 0) {
                    int abstractID = dl.insertAbstract(title, abstractContent.toString().trim());
                    if (abstractID != -1) {
                        linkAuthorsToAbstract(authors, abstractID, facultyMap);
                        System.out.println("Inserted abstract: " + title);
                    } else {
                        System.out.println("Failed to insert abstract: " + title);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    private void linkAuthorsToAbstract(String authors, int abstractID, Map<String, Integer> facultyMap) {
        String[] authorList = authors.split(",");
        for (String author : authorList) {
            String fullName = author.trim();
            Integer facultyID = facultyMap.get(fullName);
            if (facultyID != null) {
                dl.linkFacultyToAbstract(facultyID, abstractID);
            } else {
                System.out.println("Faculty member not found: " + fullName);
            }
        }
    }

    public static void main(String[] args) {
        new AbstractPresentationLayer();
    }
}