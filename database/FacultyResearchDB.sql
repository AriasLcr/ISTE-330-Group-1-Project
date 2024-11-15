-- FacultyResearchDB

DROP DATABASE IF EXISTS facultyResearchDB;

CREATE DATABASE facultyResearchDB;
USE facultyResearchDB;

-- Create Major table
DROP TABLE IF EXISTS Major;

CREATE TABLE Major (
    majorID INT PRIMARY KEY COMMENT "Unique identifier for each major",
    name VARCHAR(40) NOT NULL COMMENT "Name of the major"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores major information";

-- Create Faculty table
DROP TABLE IF EXISTS Faculty;

CREATE TABLE Faculty (
    facultyID INT PRIMARY KEY COMMENT "Unique identifier for each faculty member",
    firstName VARCHAR(30) NOT NULL COMMENT "First name of the faculty member",
    lastName VARCHAR(30) NOT NULL COMMENT "Last name of the faculty member",
    phone INT COMMENT "Phone number of the faculty member",
    email VARCHAR(55) NOT NULL UNIQUE COMMENT "Email address of the faculty member",
    building VARCHAR(4) COMMENT "Building code for the faculty member's office",
    office VARCHAR(30) COMMENT "Office location of the faculty member"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores faculty details";

-- Create Student table
DROP TABLE IF EXISTS Student;

CREATE TABLE Student (
    studentID INT PRIMARY KEY COMMENT "Unique identifier for each student",
    firstName VARCHAR(30) NOT NULL COMMENT "First name of the student",
    lastName VARCHAR(30) NOT NULL COMMENT "Last name of the student",
    phone INT COMMENT "Phone number of the student",
    email VARCHAR(55) NOT NULL UNIQUE COMMENT "Email address of the student",
    majorID INT NOT NULL COMMENT "Major ID associated with the student",
    FOREIGN KEY (majorID) REFERENCES Major(majorID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores student information";

-- Create Account table
DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
    accountID INT PRIMARY KEY COMMENT "Unique identifier for each account",
    email VARCHAR(55) NOT NULL UNIQUE COMMENT "Email associated with the account",
    password VARCHAR(40) NOT NULL COMMENT "Password for the account",
    type ENUM('Faculty', 'Student') NOT NULL COMMENT "Type of account: Faculty or Student"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores account login details";

-- Create Abstract table
DROP TABLE IF EXISTS Abstract;

CREATE TABLE Abstract (
    abstractID INT PRIMARY KEY COMMENT "Unique identifier for each abstract",
    abstractFile MEDIUMTEXT NOT NULL COMMENT "Content of the abstract"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores abstracts";

-- Create Faculty_Abstract table (Many-to-Many relationship between Faculty and Abstract)
DROP TABLE IF EXISTS Faculty_Abstract;

CREATE TABLE Faculty_Abstract (
    facultyID INT COMMENT "Identifier of the faculty member",
    abstractID INT COMMENT "Identifier of the abstract",
    PRIMARY KEY (facultyID, abstractID),
    FOREIGN KEY (facultyID) REFERENCES Faculty(facultyID) ON DELETE CASCADE,
    FOREIGN KEY (abstractID) REFERENCES Abstract(abstractID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Associates faculty members with abstracts";

-- Create Interest table
DROP TABLE IF EXISTS Interest;

CREATE TABLE Interest (
    ID INT PRIMARY KEY COMMENT "Unique identifier for each interest",
    name VARCHAR(55) NOT NULL COMMENT "Name of the interest",
    interestDescription MEDIUMTEXT COMMENT "Description of the interest"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores interests";

-- Create Faculty_Interest table (Many-to-Many relationship between Faculty and Interest)
DROP TABLE IF EXISTS Faculty_Interest;

CREATE TABLE Faculty_Interest (
    facultyID INT COMMENT "Identifier of the faculty member",
    interestID INT COMMENT "Identifier of the interest",
    PRIMARY KEY (facultyID, interestID),
    FOREIGN KEY (facultyID) REFERENCES Faculty(facultyID) ON DELETE CASCADE,
    FOREIGN KEY (interestID) REFERENCES Interest(ID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Associates faculty members with interests";

-- Create Student_Interest table (Many-to-Many relationship between Student and Interest)
DROP TABLE IF EXISTS Student_Interest;

CREATE TABLE Student_Interest (
    studentID INT COMMENT "Identifier of the student",
    interestID INT COMMENT "Identifier of the interest",
    PRIMARY KEY (studentID, interestID),
    FOREIGN KEY (studentID) REFERENCES Student(studentID) ON DELETE CASCADE,
    FOREIGN KEY (interestID) REFERENCES Interest(ID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Associates students with interests";
