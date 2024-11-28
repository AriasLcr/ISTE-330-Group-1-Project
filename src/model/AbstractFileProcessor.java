package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;

public class AbstractFileProcessor {

    public static void processDirectory(String folderPath, AbstractDAO abstractDAO, int facultyID) {
        File folder = new File(folderPath);

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
                parseAndInsertAbstract(file, abstractDAO, facultyID);
                System.out.println("Successfully processed: " + file.getName());
            } catch (Exception e) {
                System.err.println("Error processing file " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    private static void parseAndInsertAbstract(File file, AbstractDAO abstractDAO, int facultyID) throws Exception {
        String title = null;
        String author = null;
        StringBuilder abstractContent = new StringBuilder();

        // Read the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Title:")) {
                    title = line.replace("Title:", "").trim();
                } else if (line.startsWith("By:")) {
                    author = line.replace("By:", "").trim();
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

        // Save the abstract to the database and link it to the faculty
        try {
            Abstract newAbstract = new Abstract(0, title, abstractContent.toString());
            int abstractID = abstractDAO.saveAbstract(newAbstract);
            abstractDAO.linkAbstractToFaculty(facultyID, abstractID);
            System.out.println("Abstract titled '" + title + "' linked to faculty ID " + facultyID);
        } catch (SQLException e) {
            throw new Exception("Failed to save abstract to the database: " + e.getMessage());
        }
    }
}

