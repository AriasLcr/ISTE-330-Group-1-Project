/**
 * ResearchDBConnection.java
 * Group 1
 * Instructor: Jim Habermas
 * ISTE-330
 * Fall 2024
 */

import java.sql.*;

public class ResearchDBConnection {
    private static final String URI = "jdbc:mysql://localhost/facultyResearchDB?autoReconnect=true&useSSL=false";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; 
    private static final String USER = "root";
    private static final String PASSWORD = "student";
    private Connection conn;
    private static final ResearchDBConnection INSTANCE = new ResearchDBConnection();

    private ResearchDBConnection() {
        connect();  
    }

    public static ResearchDBConnection getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        return conn;  
    }

    public void connect() {
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

    public void close() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            throw new DataLayerException(e, getCurTime(), "ResearchDb:close", "Failed to close connection.");
        }
    }

    private String getCurTime() {
        return java.time.LocalDateTime.now().toString();
    }
}
