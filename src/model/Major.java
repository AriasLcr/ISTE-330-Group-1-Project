package model;

/**
 * Major.java
 * Major Model
 * Group 1: Gabriel Arias, John Arquette, Hiba Arshad, Richard Zheng
 * December 2024
 * ISTE 330
 * Instructor: Jim Habermas
 */

public class Major {
    private int majorID;
    private String name;

    public Major(int majorID, String name) {
        this.majorID = majorID;
        this.name = name;
    }

    // Getters and Setters
    public int getMajorID() {
        return majorID;
    }

    public void setMajorID(int majorID) {
        this.majorID = majorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
