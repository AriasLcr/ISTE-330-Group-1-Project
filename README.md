# Faculty Research Database

### Group 1

- Gabriel Arias
- John Arquette
- Hiba Arshad
- Richard Zheng

---

## Table of Contents

1. [Overview](#overview)
2. [Requirements](#requirements)
3. [Setting up the Project](#setting-up-the-project)
4. [Database Setup](#database-setup)
5. [Running the Application](#running-the-application)
6. [Using the Application](#using-the-application)
    - [Login](#login)
    - [Guest Access](#guest-access)
    - [Faculty Menu](#faculty-menu)
    - [Student Menu](#student-menu)
    - [Public Abstracts](#public-abstracts)

---

## Overview

The Faculty Research Database is a project designed to connect students and faculty based on shared research interests. Faculty can upload and manage their research abstracts, while students can input their research topics and find matching faculty. Additionally, public users can view available abstracts.

This project involves:

- Backend functionality with Java and MySQL.
- Interaction with a database for managing users, faculty, students, interests, and abstracts.
- A command-line interface for user interaction.

---

## Requirements

To run this project, you need:

1. **JDK**: Java Development Kit version 8 or above.
2. **MySQL**: Database management system.
3. **jGRASP**: IDE for compiling and running Java projects.
4. **FacultyResearchDB.sql**: SQL file to set up the database schema and insert test data.

---

## Setting up the Project

1. **Clone the Repository**
   - Ensure you have the project files downloaded onto your local machine.

2. **Open the Project in jGRASP**
   - Open jGRASP.
   - Navigate to `File > Open Project`.
   - Select the `FacultyResearchDatabase.gpj` file from the project directory.

3. **Compile the Project**
   - In jGRASP, ensure the `src` folder contains all Java files.
   - Compile the entire project by selecting `Project > Compile Project`.

---

## Database Setup

1. **Start MySQL Server**
   - Ensure your MySQL server is running locally.

2. **Set Up the Database**
   - Open your MySQL command-line client or MySQL Workbench.
   - Source the `FacultyResearchDB.sql` file by running:
     ```sql
     SOURCE /path/to/FacultyResearchDB.sql;
     ```
   - This will create the database, tables, and populate it with sample data.

3. **Verify the Database**
   - Log into MySQL and confirm that the `facultyResearchDB` database and its tables have been created:
     ```sql
     USE facultyResearchDB;
     SHOW TABLES;
     ```

---

## Running the Application

1. **Run the Main Program**
   - In jGRASP, open the `Controller.java` file.
   - Click `Run > Run Main Class`.

2. **Enter MySQL Password**
   - When prompted, enter the password for your MySQL database. The default password is `student`.

3. **Abstract Processing**
   - Upon startup, the application will automatically process abstracts from the `abstracts/` directory (relative to the `src` folder) and add them to the database if not already present.

---

## Using the Application

### Login

1. **Faculty or Student Login**
   - Choose option `1` from the main menu.
   - Enter your email and password to log in.
   - Faculty and students will be redirected to their respective menus.

2. **Credentials**
   - Use the sample credentials from the database:
     - **Faculty**: Email: `asubero@rit.edu`, Password: `faculty_password`
     - **Student**: Email: `johndoe@rit.edu`, Password: `student_password`

---

### Guest Access

1. Choose option `2` to log in as a guest.
2. Access available options such as viewing faculty by interests or abstracts.

---

### Faculty Menu

As a faculty member, you can:

1. **Upload an Abstract**
   - Enter the abstract title and content.
   - The abstract will be linked to your account and stored in the database.

2. **View Your Abstracts**
   - See all abstracts you have uploaded.

3. **Manage Interests**
   - Add or view interests relevant to your research.

4. **View Students**
   - Search students by major or interests.

---

### Student Menu

As a student, you can:

1. **Input Research Topics**
   - Add research topics relevant to your academic interests.

2. **View Matched Faculty**
   - See faculty members whose research aligns with your interests.

3. **View Faculty by Interest**
   - Browse faculty members by specific interests.

4. **View Abstracts**
   - Access public abstracts uploaded by faculty.

---

### Public Abstracts

1. Choose option `3` from the main menu.
2. View all abstracts marked as public by faculty members.

---

## Notes

- Ensure your MySQL server is running during application use.
- Abstracts in the `abstracts/` directory must be formatted correctly for automatic processing.
- For any issues, verify database connections and schema setup.
