package model;

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
