package model;

/**
 * Faculty.java
 * Faculty Model
 * Group 1: Gabriel Arias, John Arquette, Hiba Arshad, Richard Zheng
 * December 2024
 * ISTE 330
 * Instructor: Jim Habermas
 */

public class Faculty {
    private int facultyID;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String building;
    private String office;

    public Faculty(int facultyID, String firstName, String lastName, String phone, String email, String building, String office) {
        this.facultyID = facultyID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.building = building;
        this.office = office;
    }

    public int getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(int faculty) {
        this.facultyID = faculty;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first) {
        this.firstName = first;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last) {
        this.lastName = last;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}