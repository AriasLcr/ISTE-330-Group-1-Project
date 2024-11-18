import java.util.ArrayList;

public class FacultyController {
    private ResearchDBConnection db;
    // private PublicationManager pManager;

    /**
     * Initializes the database and publication manager
     */
    public FacultyController() {
        db = ResearchDBConnection.getInstance();
        // pManager = new PublicationManager();
    }

    /**
     * Checks if the email-password combination is valid.
     * @param email The user-entered email.
     * @param password The user-entered password.
     * @return A Faculty object if login is successful, or null if unsuccessful.
     */
    public Faculty checkLogin(String email, String password) {
        String query = "SELECT facultyID, firstName, lastName, phone, building, office "
                     + "FROM Faculty f JOIN Account a ON f.email = a.email "
                     + "WHERE a.password = md5(?) AND a.email = ?";
        try {
            ArrayList<ArrayList<String>> result = db.getData(query, getParamArrayList(password, email), false);
            if (!result.isEmpty()) {
                ArrayList<String> row = result.get(0);
                Faculty faculty = new Faculty();
                faculty.setFacultyID(Integer.parseInt(row.get(0)));
                faculty.setFirstName(row.get(1));
                faculty.setLastName(row.get(2));
                faculty.setPhone(row.get(3));
                faculty.setBuilding(row.get(4));
                faculty.setOffice(row.get(5));
                return faculty;
            }
        } catch (DataLayerException ex) {
            // Log the exception
        }
        return null;
    }

    /**
     * Retrieves abstracts associated with a faculty member.
     * @param facultyId The ID of the faculty member.
     * @return A list of abstracts authored by the faculty member.
     */
    public ArrayList<Abstract> getAbstractsByFaculty(int facultyId) {
        String query = "SELECT a.abstractID, a.title, a.abstractFile "
                     + "FROM Abstract a "
                     + "JOIN Faculty_Abstract fa ON a.abstractID = fa.abstractID "
                     + "WHERE fa.facultyID = ?";
        ArrayList<Abstract> abstracts = new ArrayList<>();
        try {
            ArrayList<ArrayList<String>> result = db.getData(query, getParamArrayList(String.valueOf(facultyId)), false);
            for (ArrayList<String> row : result) {
                Abstract abstractObj = new Abstract();
                abstractObj.setId(Integer.parseInt(row.get(0)));
                abstractObj.setTitle(row.get(1));
                abstractObj.setContent(row.get(2));
                abstracts.add(abstractObj);
            }
        } catch (DataLayerException ex) {
            // Log the exception
        }
        return abstracts;
    }

    /**
     * Adds a new abstract and associates it with faculty members.
     * @param title The title of the abstract.
     * @param content The content of the abstract.
     * @param facultyIds List of faculty IDs to associate with the abstract.
     * @return A message indicating the operation's status.
     */
    public String addAbstract(String title, String content, ArrayList<Integer> facultyIds) {
        String insertAbstractQuery = "INSERT INTO Abstract (title, abstractFile) VALUES (?, ?)";
        try {
            db.startTrans(); // Start transaction
            int abstractId = db.insertData(insertAbstractQuery, getParamArrayList(title, content));
            if (abstractId <= 0) {
                db.rollbackTrans(); // Rollback on failure
                return "Error: Failed to add abstract.";
            }
    
            String linkFacultyQuery = "INSERT INTO Faculty_Abstract (facultyID, abstractID) VALUES (?, ?)";
            for (int facultyId : facultyIds) {
                if (!db.setData(linkFacultyQuery, getParamArrayList(String.valueOf(facultyId), String.valueOf(abstractId)))) {
                    db.rollbackTrans(); // Rollback on failure
                    return "Error: Failed to associate faculty with the abstract.";
                }
            }
    
            db.commitTrans(); // Commit transaction on success
            return "Abstract successfully added and associated with faculty!";
        } catch (DataLayerException ex) {
            try {
                db.rollbackTrans(); // Handle rollback exception explicitly
            } catch (DataLayerException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            return "Error: Unable to add abstract due to an exception: " + ex.getMessage();
        } finally {
            try {
                db.endTrans(); // Handle end transaction exception explicitly
            } catch (DataLayerException endTransEx) {
                System.err.println("Error during ending transaction: " + endTransEx.getMessage());
            }
        }
    }

    /**
     * Retrieves faculty details, including interests and associated abstracts.
     * @param facultyId The ID of the faculty member.
     * @return A Faculty object populated with the member's details, interests, and abstracts.
     */
    public Faculty getFacultyDetails(int facultyId) {
        String facultyQuery = "SELECT facultyID, firstName, lastName, phone, building, office FROM Faculty WHERE facultyID = ?";
        String interestQuery = "SELECT i.name, i.interestDescription "
                             + "FROM Interest i JOIN Faculty_Interest fi ON i.ID = fi.interestID "
                             + "WHERE fi.facultyID = ?";
        Faculty faculty = new Faculty();

        try {
            // Fetch faculty basic details
            ArrayList<ArrayList<String>> facultyResult = db.getData(facultyQuery, getParamArrayList(String.valueOf(facultyId)), false);
            if (!facultyResult.isEmpty()) {
                ArrayList<String> row = facultyResult.get(0);
                faculty.setFacultyID(Integer.parseInt(row.get(0)));
                faculty.setFirstName(row.get(1));
                faculty.setLastName(row.get(2));
                faculty.setPhone(row.get(3));
                faculty.setBuilding(row.get(4));
                faculty.setOffice(row.get(5));
            }

            // Fetch faculty interests
            ArrayList<ArrayList<String>> interestResult = db.getData(interestQuery, getParamArrayList(String.valueOf(facultyId)), false);
            ArrayList<Interest> interests = new ArrayList<>();
            for (ArrayList<String> row : interestResult) {
                Interest interest = new Interest();
                interest.setName(row.get(0));
                interest.setDescription(row.get(1));
                interests.add(interest);
            }
            faculty.setInterests(interests);

            // Fetch faculty abstracts
            faculty.setAbstracts(getAbstractsByFaculty(facultyId));
        } catch (DataLayerException ex) {
            // Log the exception
        }
        return faculty;
    }

    /**
     * Fills an ArrayList with a series of Strings.
     * @return An ArrayList containing the given Strings.
     */
    private ArrayList<String> getParamArrayList(String... params) {
        ArrayList<String> paramList = new ArrayList<>();
        for (String param : params) {
            paramList.add(param);
        }
        return paramList;
    }
}
