package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * DBConnection.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * Singleton class that manages the MySQL database connection.
 * All DAO classes call DBConnection.getConnection() to get the connection.
 *
 * Setup: Create a file called config.properties in the project root with:
 *   db.url=jdbc:mysql://localhost:3306/employeeData
 *   db.user=root
 *   db.password=yourpassword
 */
public class DBConnection {

    private static Connection connection = null;

    // Private constructor — no one can instantiate this class
    private DBConnection() {}

    /**
     * Returns a connection to the employeeData MySQL database.
     * Reads credentials from config.properties file.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Properties props = new Properties();

                // Try to load from config.properties file first
                try {
                    FileInputStream fis = new FileInputStream("config.properties");
                    props.load(fis);
                    fis.close();

                    String url      = props.getProperty("db.url");
                    String user     = props.getProperty("db.user");
                    String password = props.getProperty("db.password");

                    connection = DriverManager.getConnection(url, user, password);

                } catch (IOException e) {
                    // If config.properties not found, use hardcoded defaults
                    System.out.println("[DBConnection] config.properties not found. Using default settings.");
                    System.out.println("[DBConnection] Create a config.properties file in the project root to override.");

                    String url      = "jdbc:mysql://localhost:3306/employeeData?allowPublicKeyRetrieval=true&useSSL=false";
                    String user     = "";
                    String password = "";  // ← Each teammate sets their own password here if not using config.properties

                    connection = DriverManager.getConnection(url, user, password);
                }

                System.out.println("[DBConnection] Connected to employeeData database successfully.");
            }
        } catch (SQLException e) {
            System.out.println("[DBConnection] ERROR: Could not connect to database.");
            System.out.println("[DBConnection] Make sure MySQL is running and employeeData database exists.");
            System.out.println("[DBConnection] Details: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the database connection.
     * Call this when the application exits.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DBConnection] Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("[DBConnection] ERROR closing connection: " + e.getMessage());
        }
    }
}
