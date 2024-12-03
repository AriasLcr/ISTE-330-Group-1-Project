package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbstractFileProcessor {

    public static void processDirectory(String folderPath, AbstractDAO abstractDAO, FacultyDAO facultyDAO) {
        File folder = new File(folderPath);

        System.out.println("Folder Path: " + folderPath);
        System.out.println("Folder Exists: " + folder.exists());

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: The specified folder does not exist or is not a directory!");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt")); // Filter for .txt files

        if (files == null || files.length == 0) {
            System.out.println("No abstract files found in the directory.");
            return;
        }

        for (File file : files) {
            System.out.println("Processing file: " + file.getName());
            try {
                parseAndInsertAbstract(file, abstractDAO, facultyDAO);
                System.out.println("Successfully processed: " + file.getName());
            } catch (Exception e) {
                System.err.println("Error processing file " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    private static void parseAndInsertAbstract(File file, AbstractDAO abstractDAO, FacultyDAO facultyDAO) throws Exception {
        String title = null;
        String authorLine = null;
        StringBuilder abstractContent = new StringBuilder();

        // Read the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Title:")) {
                    title = line.replace("Title:", "").trim();
                } else if (line.startsWith("By:")) {
                    authorLine = line.replace("By:", "").trim();
                } else if (line.startsWith("Abstract:")) {
                    abstractContent.append(line.replace("Abstract:", "").trim()).append(" ");
                } else {
                    abstractContent.append(line.trim()).append(" ");
                }
            }
        }

        if (title == null || abstractContent.length() == 0) {
            throw new Exception("Invalid file format: Missing title or abstract content.");
        }

        // Check if the abstract already exists
        if (abstractDAO.abstractExists(title, abstractContent.toString())) {
            System.out.println("Abstract titled '" + title + "' already exists. Skipping.");
            return;
        }

        // Save the abstract to the database
        int abstractID;
        try {
            Abstract newAbstract = new Abstract(0, title, abstractContent.toString());
            abstractID = abstractDAO.saveAbstract(newAbstract);
            System.out.println("Abstract titled '" + title + "' saved to the database.");
        } catch (SQLException e) {
            throw new Exception("Failed to save abstract to the database: " + e.getMessage());
        }

        // Link authors to the abstract
        if (authorLine != null) {
            String[] authors = authorLine.split(","); // Split authors by commas
            List<String[]> authorNames = new ArrayList<>();

            for (String author : authors) {
                String[] nameParts = author.trim().split(" "); // Split first and last name
                if (nameParts.length >= 2) {
                    authorNames.add(new String[]{nameParts[0], nameParts[1]}); // Add first and last name
                }
            }

            for (String[] name : authorNames) {
                String firstName = name[0];
                String lastName = name[1];

                // Get the faculty ID based on the name
                int facultyID = facultyDAO.getFacultyIDByName(firstName, lastName);

                if (facultyID > 0) {
                    try {
                        abstractDAO.linkAbstractToFaculty(facultyID, abstractID);
                        System.out.println("Linked abstract '" + title + "' to faculty: " + firstName + " " + lastName);
                    } catch (SQLException e) {
                        System.err.println("Failed to link abstract '" + title + "' to faculty: " + e.getMessage());
                    }
                } else {
                    System.out.println("Faculty member '" + firstName + " " + lastName + "' not found in the database. Skipping link.");
                }
            }
        }
    }
}
