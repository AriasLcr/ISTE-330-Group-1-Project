package model;

public class Interest {
    private int interestID;
    private String name;
    private String description;

    public Interest(int interestID, String name, String description) {
        this.interestID = interestID;
        this.name = name;
        this.description = description;
    }

    public int getInterestID() {
        return interestID;
    }

    public void setInterestID(int interestID) {
        this.interestID = interestID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
