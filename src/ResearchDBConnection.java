/**
 * ResearchDBConnection.java
 * Group 1
 * Instructor: Jim Habermas
 * ISTE-330
 * Fall 2024
 */

import java.sql.*;
import java.util.ArrayList;

public class ResearchDBConnection {
    private static final String URI = "jdbc:mysql://localhost/facultyResearchDB?autoReconnect=true&useSSL=false";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // Updated driver
    private static final String USER = "root";
    private static final String PASSWORD = "student";
    private Connection conn;
    private static final ResearchDBConnection INSTANCE = new ResearchDBConnection();

    public static ResearchDBConnection getInstance() {
        return INSTANCE;
    }

    public void connect() throws DataLayerException {
        if (conn == null) {
            try {
                Class.forName(DRIVER);
                conn = DriverManager.getConnection(URI, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new DataLayerException(e, getCurTime(), "ResearchDb:connect", "MySQL driver not found.");
            } catch (SQLException e) {
                throw new DataLayerException(e, getCurTime(), "ResearchDb:connect", "Connection failed.");
            }
        }
    }

    public void close() throws DataLayerException {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            throw new DataLayerException(e, getCurTime(), "ResearchDb:close", "Failed to close connection.");
        }
    }

    public ArrayList<ArrayList<String>> getData(String prepStr, ArrayList<String> values, boolean includeColNames) throws DataLayerException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
    
        // Establish connection and prepare statement
        connect();
        try (PreparedStatement stmt = prepare(prepStr, values);
             ResultSet result = stmt.executeQuery()) {
    
            ResultSetMetaData rsmd = result.getMetaData();
            int numCols = rsmd.getColumnCount();
    
            // Add column names if required
            if (includeColNames) {
                data.add(getColumnNames(rsmd, numCols));
            }
    
            // Process rows from the result set
            while (result.next()) {
                data.add(getRowData(result, numCols));
            }
    
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, getCurTime(), "ResearchDb:getData(String,ArrayList)",
                                  "Prepared string: " + prepStr, "Values: " + values);
        } catch (Exception e) {
            throw new DataLayerException(e, getCurTime(), "ResearchDb:getData(String,ArrayList)",
                                  "Prepared string: " + prepStr, "Values: " + values);
        } finally {
            close();
        }
        return data;
    }
    
    // Helper method to fetch column names
    private ArrayList<String> getColumnNames(ResultSetMetaData rsmd, int numCols) throws SQLException {
        ArrayList<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= numCols; i++) {
            columnNames.add(rsmd.getColumnName(i));
        }
        return columnNames;
    }
    
    // Helper method to fetch a single row of data
    private ArrayList<String> getRowData(ResultSet result, int numCols) throws SQLException {
        ArrayList<String> row = new ArrayList<>();
        for (int i = 1; i <= numCols; i++) {
            row.add(result.getString(i));
        }
        return row;
    }
    
    private String getCurTime() {
        return java.time.LocalDateTime.now().toString();
    }

    public boolean setData(String sqlStr, ArrayList<String> values) throws DataLayerException {
        try (PreparedStatement stmt = prepare(sqlStr, values)) {
            int rowCount = stmt.executeUpdate();
            return rowCount > 0;
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, "ResearchDb:setData", getCurTime(), "SQL: " + sqlStr, "Values: " + values);
        }
    }

    /**
     * Executes an INSERT statement and retrieves the generated key for the new row.
     * @param sqlStr The SQL query with placeholders.
     * @param values The values to bind to the placeholders.
     * @return The generated ID of the new record, or -1 if unsuccessful.
     */
    public int insertData(String sqlStr, ArrayList<String> values) throws DataLayerException {
        try (PreparedStatement stmt = prepare(sqlStr, values)) {
            stmt.executeUpdate();
            try (ResultSet result = stmt.getGeneratedKeys()) {
                if (result.next()) {
                    return result.getInt(1);
                }
            }
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, "ResearchDb:insertData", getCurTime(), "SQL: " + sqlStr, "Values: " + values);
        }
        return -1;
    }

    /**
     * Prepares a SQL statement and binds values to its placeholders.
     * @param prepStr The SQL query with placeholders.
     * @param values The values to bind to the placeholders.
     * @return The prepared statement ready for execution.
     */
    public PreparedStatement prepare(String prepStr, ArrayList<String> values) throws DataLayerException {
        try {
            PreparedStatement stmt = conn.prepareStatement(prepStr, Statement.RETURN_GENERATED_KEYS);
            bindValues(stmt, values);
            return stmt;
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, "ResearchDb:prepare", getCurTime(), "Prepared string: " + prepStr, "Values: " + values);
        }
    }

    /**
     * Binds values to a PreparedStatement.
     * @param stmt The prepared statement.
     * @param values The values to bind.
     * @throws SQLException If an error occurs during binding.
     */
    private void bindValues(PreparedStatement stmt, ArrayList<String> values) throws SQLException {
        for (int i = 0; i < values.size(); i++) {
            stmt.setString(i + 1, values.get(i)); // Bind values (1-based index)
        }
    }

    /**
     * Begins a transaction.
     */
    public void startTrans() throws DataLayerException {
        setAutoCommit(false, "startTrans");
    }

    /**
     * Ends a transaction.
     */
    public void endTrans() throws DataLayerException {
        setAutoCommit(true, "endTrans");
    }

    /**
     * Rolls back changes made during a transaction.
     */
    public void rollbackTrans() throws DataLayerException {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, getCurTime(), "ResearchDb:rollbackTrans");
        }
    }

    /**
     * Commits changes made during a transaction.
     */
    public void commitTrans() throws DataLayerException {
        try {
            if (conn != null) {
                conn.commit();
            }
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, getCurTime(), "ResearchDb:commitTrans");
        }
    }

    /**
     * Helper method to set the auto-commit mode.
     * @param autoCommit True to enable auto-commit, false to disable.
     * @param operation The name of the operation for error tracking.
     */
    private void setAutoCommit(boolean autoCommit, String operation) throws DataLayerException {
        try {
            if (conn != null) {
                conn.setAutoCommit(autoCommit);
            }
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, getCurTime(), "ResearchDb:" + operation);
        }
    }
}