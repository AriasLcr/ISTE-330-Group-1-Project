# ISTE-330-Group-1-Project

# Abstract Management System

## Index
1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Setup Instructions](#setup-instructions)
4. [How to Use the Application](#how-to-use-the-application)
5. [Key Features](#key-features)
6. [Configuration](#configuration)
7. [Error Handling](#error-handling)
8. [Sample Output](#sample-output)

## Overview
This project consists of two main Java classes: `AbstractDataLayer.java` and `AbstractPresentationLayer.java`. The purpose of these classes is to connect to the `FacultyResearchDB` database, read abstract data from text files, and store the abstracts and their associations with faculty in the database. The `AbstractPresentationLayer` acts as the user interface, while the `AbstractDataLayer` handles database operations.

## Prerequisites
1. **Database**: Make sure the `FacultyResearchDB` database is created and set up using the provided SQL script.
2. **JDBC Driver**: Ensure the MySQL JDBC driver (`mysql-connector-java`) is available and added to your project’s classpath.
3. **Abstract Files**: Prepare a directory containing `.txt` files for abstracts. Each file should have the following format:
   ```
   Title: Example Abstract Title
   By: Author Name
   Abstract: This is the content of the abstract...
   ```
4. **Java Environment**: Java 8 or later installed.

## Setup Instructions
1. Compile the `AbstractDataLayer.java` and `AbstractPresentationLayer.java` files:
   ```bash
   javac AbstractDataLayer.java AbstractPresentationLayer.java
   ```
2. Run the `AbstractPresentationLayer` program:
   ```bash
   java AbstractPresentationLayer
   ```

## How to Use the Application
1. **Database Connection**: Upon running the `AbstractPresentationLayer` program, you will be prompted to enter your MySQL database password. If you leave it blank, the default password `student` will be used.
2. **Provide Abstract Directory Path**: The program will ask you to enter the directory path containing the `.txt` files for abstracts. Ensure the files are correctly formatted.
3. **Abstract Ingestion**:
   - The program reads each `.txt` file, extracts the title, author(s), and content.
   - The data is inserted into the `Abstract` table. If the title and content already exist, it skips duplicate entries.
   - It associates the abstract with existing faculty based on author names.

## Key Features
- **Automatic Database Connection Handling**: Uses a `connect` method in the `AbstractDataLayer` to establish a connection with the MySQL database.
- **Duplicate Handling**: Prevents duplicate abstracts from being inserted.
- **Hidden Password Input**: Uses a graphical prompt to input the database password securely.
- **Author-Abstract Association**: Links abstracts to faculty members in the database.

## Configuration
- **Database Credentials**: Modify the username and default password in the `setupDatabaseConnection()` method of `AbstractPresentationLayer.java` as needed.

## Error Handling
- If a faculty member cannot be found in the database when linking to an abstract, a message will be displayed.
- File reading errors, database connection errors, and SQL-related errors are handled with appropriate messages.

## Sample Output
When running the program, you may see outputs like:
```
Enter database password: ****
Enter the directory path containing abstract files: C:\path\to\abstracts
Connected to the database.
Inserted abstract: Example Abstract Title
Faculty member not found: John Doe
Link between facultyID 1 and abstractID 5 already exists.
```

----

