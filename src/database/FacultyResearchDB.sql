-- FacultyResearchDB

DROP DATABASE IF EXISTS facultyResearchDB;

CREATE DATABASE facultyResearchDB;
USE facultyResearchDB;

-- Create Major table
DROP TABLE IF EXISTS Major;

CREATE TABLE Major (
    majorID INT PRIMARY KEY AUTO_INCREMENT  COMMENT "Unique identifier for each major",
    name VARCHAR(100) NOT NULL COMMENT "Name of the major"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores major information";

-- Create Faculty table
DROP TABLE IF EXISTS Faculty;

CREATE TABLE Faculty (
    facultyID INT PRIMARY KEY AUTO_INCREMENT  COMMENT "Unique identifier for each faculty member",
    firstName VARCHAR(30) NOT NULL COMMENT "First name of the faculty member",
    lastName VARCHAR(30) NOT NULL COMMENT "Last name of the faculty member",
    phone VARCHAR(15) COMMENT "Phone number of the faculty member, format '(xxx) xxx-xxxx'",
    email VARCHAR(55) NOT NULL UNIQUE COMMENT "Email address of the faculty member",
    building VARCHAR(4) COMMENT "Building code for the faculty member's office",
    office VARCHAR(30) COMMENT "Office location of the faculty member"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores faculty details";

-- Create Student table
DROP TABLE IF EXISTS Student;

CREATE TABLE Student (
    studentID INT PRIMARY KEY AUTO_INCREMENT  COMMENT "Unique identifier for each student",
    firstName VARCHAR(30) NOT NULL COMMENT "First name of the student",
    lastName VARCHAR(30) NOT NULL COMMENT "Last name of the student",
    phone VARCHAR(15) COMMENT "Phone number of the student, format '(xxx) xxx-xxxx'",
    email VARCHAR(55) NOT NULL UNIQUE COMMENT "Email address of the student",
    majorID INT NOT NULL COMMENT "Major ID associated with the student",
    FOREIGN KEY (majorID) REFERENCES Major(majorID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores student information";

-- Create Account table
DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
    accountID INT PRIMARY KEY AUTO_INCREMENT  COMMENT "Unique identifier for each account",
    email VARCHAR(55) NOT NULL UNIQUE COMMENT "Email associated with the account",
    password VARCHAR(60) NOT NULL COMMENT "Password for the account",
    type ENUM('Faculty', 'Student') NOT NULL COMMENT "Type of account: Faculty or Student"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = "Stores account login details";

-- Create Abstract table
DROP TABLE IF EXISTS Abstract;

CREATE TABLE Abstract (
    abstractID INT PRIMARY KEY AUTO_INCREMENT  COMMENT "Unique identifier for each abstract",
    title VARCHAR(100) NOT NULL COMMENT "Title of the abstract",
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
    ID INT AUTO_INCREMENT PRIMARY KEY COMMENT "Unique identifier for each interest",
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

-- Insert Faculty Data
INSERT INTO Faculty (firstName, lastName, phone, email, building, office) VALUES
('Armstrong', 'Subero', '(585) 111-1111', 'asubero@rit.edu', 'GOL', '2315'),
('Ashish', 'Gupta', '(585) 111-1112', 'agupta@rit.edu', 'GOL', '2621'),
('Barbara', 'Ericson', '(585) 111-1113', 'bericson@rit.edu', 'GOL', '2111'),
('Carlos', 'Oliveira', '(585) 111-1114', 'coliveira@rit.edu', 'GOL', '2615'),
('David', 'Sutton', '(585) 111-1115', 'dsutton@rit.edu', 'GOL', 'online'),
('George', 'Defenbaugh', '(585) 111-1116', 'gdefenbaugh@rit.edu', 'GOL', '2627'),
('Richard', 'Smedley', '(585) 111-1117', 'rsmedley@rit.edu', 'GOL', '2619'),
('Jim', 'Habermas', '(585) 746-9331', 'jhabermas@rit.edu', 'GOL', 'online'),
('Jonathan', 'Bartlett', '(585) 111-1119', 'jbartlett@rit.edu', 'GOL', 'online'),
('Kishori', 'Sharan', '(585) 111-1110', 'ksharan@rit.edu', 'GOL', '2617'),
('Mark', 'Simon', '(585) 111-1111', 'msimon@rit.edu', 'GOL', '2443'),
('Michael', 'Coughlan', '(585) 111-1112', 'mcoughlan@rit.edu', 'GOL', '2645'),
('Michael', 'Inden', '(585) 111-1123', 'minden@rit.edu', 'GOL', 'online'),
('Slobodan', 'Dmitrovic', '(585) 111-1114', 'sdmitrovic@rit.edu', 'GOL', '2323'),
('Svein', 'Linge', '(585) 111-1115', 'slinge@rit.edu', 'GOL', '2655'),
('Hans', 'Langtangen', '(585) 111-1116', 'hlangtangen@rit.edu', 'GOL', '2518'),
('Vaskaran', 'Sarcar', '(585) 111-1117', 'vsarcar@rit.edu', 'GOL', '2651');

-- Stored Procedure to Display Abstracts and Associated Authors with Truncated Content
DROP PROCEDURE IF EXISTS GetAbstractsInfo;
DELIMITER $$

CREATE PROCEDURE GetAbstractsInfo() 
BEGIN
   SELECT
      a.title AS `Abstract Title`,
      GROUP_CONCAT(CONCAT(f.lastName, ', ', f.firstName) 
                   ORDER BY f.lastName SEPARATOR ' | ') AS `Author(s)`,
      CONCAT(SUBSTRING_INDEX(a.abstractFile, ' ', 15), '...') AS `Abstract (truncated)`
   FROM
      Abstract a 
   LEFT JOIN
      Faculty_Abstract fa ON a.abstractID = fa.abstractID 
   LEFT JOIN
      Faculty f ON fa.facultyID = f.facultyID 
   GROUP BY
      a.abstractID, a.title, a.abstractFile 
   ORDER BY
      a.abstractID;
END$$
DELIMITER ;

-- Insert Majors into Major table
INSERT INTO Major (name) VALUES
('Computer Science'),
('Software Engineering'),
('Game Design and Development'),
('Human-Centered Computing'),
('Computing Security'),
('Data Science'),
('Web and Mobile Computing'),
('Artificial Intelligence and Machine Learning');

-- Insert Students into Student table
INSERT INTO Student (firstName, lastName, phone, email, majorID) VALUES
('John', 'Doe', '(585) 123-4567', 'johndoe@rit.edu', 1),
('Jane', 'Smith', '(585) 765-4321', 'janesmith@rit.edu', 2),
('Alex', 'Johnson', '(585) 234-5678', 'alexjohnson@rit.edu', 3),
('Emily', 'Davis', '(585) 345-6789', 'emilydavis@rit.edu', 4),
('Chris', 'Brown', '(585) 456-7890', 'chrisbrown@rit.edu', 5),
('Sam', 'Taylor', '(585) 567-8901', 'samtaylor@rit.edu', 6),
('Taylor', 'Anderson', '(585) 678-9012', 'tayloranderson@rit.edu', 7),
('Jordan', 'Lee', '(585) 789-0123', 'jordanlee@rit.edu', 8);

-- Insert Interests into Interest table
INSERT INTO Interest (name, interestDescription) VALUES
('Cybersecurity', 'Focus on security threats and mitigation'),
('Artificial Intelligence', 'Building intelligent systems'),
('Game Development', 'Creating and designing video games'),
('Human-Computer Interaction', 'Improving interaction between humans and computers'),
('Mobile Development', 'Developing mobile applications'),
('Data Analysis', 'Analyzing and interpreting data'),
('Web Development', 'Building and maintaining websites'),
('Machine Learning', 'Training systems to learn from data');

-- Link Students to Interests in Student_Interest table
INSERT INTO Student_Interest (studentID, interestID) VALUES
(1, 2), -- John Doe is interested in Artificial Intelligence
(1, 6), -- John Doe is also interested in Data Analysis
(2, 1), -- Jane Smith is interested in Cybersecurity
(3, 3), -- Alex Johnson is interested in Game Development
(4, 4), -- Emily Davis is interested in Human-Computer Interaction
(5, 5), -- Chris Brown is interested in Mobile Development
(6, 8), -- Sam Taylor is interested in Machine Learning
(7, 7), -- Taylor Anderson is interested in Web Development
(8, 2); -- Jordan Lee is interested in Artificial Intelligence

-- Link Faculty to Interests in Faculty_Interest table
INSERT INTO Faculty_Interest (facultyID, interestID) VALUES
(1, 2), -- Armstrong Subero specializes in Artificial Intelligence
(1, 6), -- Armstrong Subero also specializes in Data Analysis
(2, 1), -- Ashish Gupta specializes in Cybersecurity
(3, 3), -- Barbara Ericson specializes in Game Development
(4, 4), -- Carlos Oliveira specializes in Human-Computer Interaction
(5, 5), -- David Sutton specializes in Mobile Development
(6, 8), -- George Defenbaugh specializes in Machine Learning
(7, 7); -- Richard Smedley specializes in Web Development


-- Populate Account table for Faculty
INSERT INTO Account (email, password, type) 
SELECT email, 'faculty_password', 'Faculty'
FROM Faculty;

-- Populate Account table for Students
INSERT INTO Account (email, password, type) 
SELECT email, 'student_password', 'Student'
FROM Student;