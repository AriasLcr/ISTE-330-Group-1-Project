import java.util.ArrayList;
import java.util.List;

/**
 * Faculty.java
 * Group 1
 * Instructor: Jim Habermas
 * ISTE-330
 * Fall 2024
 */

public class Faculty {

    private int facultyID;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String building;
    private String office;
    private List<Abstract> abstracts;
    private List<Interest> interests;

    public Faculty() {}

    /**
     * Faculty Constructor
     * @param facultyID  the ID of the faculty member
     * @param firstName  the first name of the faculty member
     * @param lastName   the last name of the faculty member
     * @param phone      the phone number of the faculty member
     * @param email      the email address of the faculty member
     * @param building   the building where the faculty member's office is located
     * @param office     the office number of the faculty member
     */
    public Faculty(int facultyID, String firstName, String lastName, String phone, String email, String building, String office) {
        this.facultyID = facultyID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.building = building;
        this.office = office;
        this.interests = new ArrayList<>();
        this.abstracts = new ArrayList<>();
    }

    /** 
     * Accessors and Mutators
     */

    public int getFacultyID() {return facultyID;}

    public void setFacultyID(int facultyID) {this.facultyID = facultyID;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getBuilding() {return building;}

    public void setBuilding(String building) {this.building = building;}

    public String getOffice() {return office;}

    public void setOffice(String office) {this.office = office;}

    // Getters and Setters for Interests
    public List<Interest> getInterests() {return interests;}

    public void setInterests(List<Interest> interests) {this.interests = interests;}

    public void addInterest(Interest interest) {this.interests.add(interest);}

    public void removeInterest(Interest interest) {this.interests.remove(interest);}

    // Getters and Setters for Abstracts
    public List<Abstract> getAbstracts() {return abstracts;}

    public void setAbstracts(List<Abstract> abstracts) {this.abstracts = abstracts;}

    public void addAbstract(Abstract abstractObj) {this.abstracts.add(abstractObj);}

    public void removeAbstract(Abstract abstractObj) {this.abstracts.remove(abstractObj);}

}
