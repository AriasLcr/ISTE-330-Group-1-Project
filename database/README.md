# ISTE-330-Group-1-Project

# FacultyResearchDB.sql

**FacultyResearchDB** is a structured SQL database designed to facilitate collaboration between faculty, students, and external users through efficient storage of faculty abstracts, student interests, and user account management. This README provides a comprehensive overview of the database, its schema, setup, and usage.

----

## Table of Contents

- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Database Schema](#database-schema)
  - [Major Table](#major-table)
  - [Faculty Table](#faculty-table)
  - [Student Table](#student-table)
  - [Account Table](#account-table)
  - [Abstract Table](#abstract-table)
  - [Faculty_Abstract Table](#faculty_abstract-table)
  - [Interest Table](#interest-table)
  - [Faculty_Interest Table](#faculty_interest-table)
  - [Student_Interest Table](#student_interest-table)
- [Usage](#usage)
- [Maintenance and Modifications](#maintenance-and-modifications)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

----

## Prerequisites

Before setting up the database, ensure you have the following:

**MySQL Server 8.0+** (or compatible)
**MySQL Workbench** (optional, for graphical database management)
**Git** (for version control, if cloning from GitHub)

----

## Setup

### 1. Clone the Repository

```bash
git clone https://github.com/AriasLcr/ISTE-330-Group-1-Project.git
```

### 2. Navigate to the `database` Directory

```bash
cd ISTE-330-Group-1-Project/database
```

### 3. Execute the SQL Script

- Execute `FacultyResearchDB.sql` in command line.

----

## Database Schema

### Major Table

- **Description**: Stores information about majors offered to students.
- **Columns**:
  - `majorID` (INT, Primary Key) - Unique identifier for each major.
  - `name` (VARCHAR(40)) - Name of the major.

----

### Faculty Table

- **Description**: Contains details of faculty members.
- **Columns**:
  - `facultyID` (INT, Primary Key) - Unique identifier.
  - `firstName` (VARCHAR(30)) - First name.
  - `lastName` (VARCHAR(30)) - Last name.
  - `phone` (INT) - Phone number.
  - `email` (VARCHAR(55), UNIQUE) - Email address.
  - `building` (VARCHAR(4)) - Building code.
  - `office` (VARCHAR(30)) - Office location.

----

### Student Table

- **Description**: Stores information about students and their majors.
- **Columns**:
  - `studentID` (INT, Primary Key) - Unique identifier.
  - `firstName` (VARCHAR(30)) - First name.
  - `lastName` (VARCHAR(30)) - Last name.
  - `phone` (INT) - Phone number.
  - `email` (VARCHAR(55), UNIQUE) - Email address.
  - `majorID` (INT, Foreign Key) - Links to `Major(majorID)`.

----

### Account Table

- **Description**: Manages user accounts.
- **Columns**:
  - `accountID` (INT, Primary Key) - Unique identifier.
  - `email` (VARCHAR(55), UNIQUE) - Email associated with the account.
  - `password` (VARCHAR(40)) - Account password.
  - `type` (ENUM('Faculty', 'Student')) - Account type.

----

### Abstract Table

- **Description**: Stores faculty abstracts.
- **Columns**:
  - `abstractID` (INT, Primary Key) - Unique identifier.
  - `abstractFile` (MEDIUMTEXT) - Abstract content.

----

### Faculty_Abstract Table

- **Description**: Many-to-many relationship between `Faculty` and `Abstract`.
- **Columns**:
  - `facultyID` (INT, Foreign Key) - References `Faculty(facultyID)`.
  - `abstractID` (INT, Foreign Key) - References `Abstract(abstractID)`.

----

### Interest Table

- **Description**: Stores faculty and student interests.
- **Columns**:
  - `ID` (INT, Primary Key) - Unique identifier.
  - `name` (VARCHAR(55)) - Interest name.
  - `interestDescription` (MEDIUMTEXT) - Description.

----

### Faculty_Interest Table

- **Description**: Many-to-many relationship between `Faculty` and `Interest`.
- **Columns**:
  - `facultyID` (INT, Foreign Key) - References `Faculty(facultyID)`.
  - `interestID` (INT, Foreign Key) - References `Interest(ID)`.

----

### Student_Interest Table

- **Description**: Many-to-many relationship between `Student` and `Interest`.
- **Columns**:
  - `studentID` (INT, Foreign Key) - References `Student(studentID)`.
  - `interestID` (INT, Foreign Key) - References `Interest(ID)`.

----

## Usage

- **Connecting to the Database**: Use MySQL clients or JDBC for connecting applications.
- **Operations**: Manage data using CRUD (Create, Read, Update, Delete) operations on tables.
- **Integration**: Integrate this database with Java programs for additional functionality (e.g., account creation).

----

## Maintenance and Modifications

- **Schema Updates**: Modify `FacultyResearchDB.sql` for schema changes.
- **Backup**: Always back up existing data before applying major changes.

----

## Troubleshooting

- **Connection Issues**: Ensure the MySQL server is running and credentials are correct.
- **Syntax Errors**: Check compatibility issues and verify SQL syntax.

----

## Contributing

- Use the standard GitHub workflow (fork, clone, branch, commit, push, pull request) for contributing changes.
- Report issues or request new features via GitHub issues.

----