package util;

import dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * AuthManager.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * Handles user authentication — login, role checking, and session management.
 * Uses SHA-256 password hashing for security.
 *
 * NOTE: Requires a 'users' table in the database:
 *   CREATE TABLE users (
 *     userID   INT AUTO_INCREMENT PRIMARY KEY,
 *     username VARCHAR(50) NOT NULL UNIQUE,
 *     password VARCHAR(64) NOT NULL,   -- SHA-256 hash
 *     role     ENUM('ADMIN','EMPLOYEE') NOT NULL,
 *     empid    INT,
 *     FOREIGN KEY (empid) REFERENCES employees(empid)
 *   );
 */
public class AuthManager {

    // Maximum login attempts before system exits
    private static final int MAX_ATTEMPTS = 5;

    // Stores currently logged in user's info
    private static String currentUsername = null;
    private static String currentRole     = null;
    private static int    currentEmpID    = -1;

    // ─── Authentication ───────────────────────────────────────────────────────

    /**
     * Attempts to log in with given username and password.
     * Returns true if successful, false if credentials are wrong.
     *
     * @param username entered username
     * @param password entered plain text password (will be hashed)
     * @return true if login successful
     */
    public static boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("  ERROR: Username cannot be empty.");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("  ERROR: Password cannot be empty.");
            return false;
        }
        if (password.length() < 8) {
            System.out.println("  ERROR: Password must be at least 8 characters.");
            return false;
        }

        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) return false;

        String sql = "SELECT empid, role FROM users WHERE username = ? AND password = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username.trim());
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                currentUsername = username.trim();
                currentRole     = rs.getString("role");
                currentEmpID    = rs.getInt("empid");
                System.out.println("\n  Login successful! Welcome, " + currentUsername + ".");
                System.out.println("  Role: " + currentRole);
                return true;
            } else {
                System.out.println("  ERROR: Invalid username or password. Please try again.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("  ERROR: Database error during login: " + e.getMessage());
            return false;
        }
    }

    /**
     * Runs the full login loop with attempt tracking.
     * Exits program after MAX_ATTEMPTS failed attempts.
     *
     * @return true if login succeeded, false if max attempts reached
     */
    public static boolean runLoginLoop(java.util.Scanner scanner) {
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            System.out.println("\n========================================");
            System.out.println("Employee Management System - Login");
            System.out.println("========================================");
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (login(username, password)) {
                return true;
            }

            attempts++;
            int remaining = MAX_ATTEMPTS - attempts;
            if (remaining > 0) {
                System.out.println("  Attempts remaining: " + remaining);
            } else {
                System.out.println("\n  Too many failed attempts. System will now exit.");
                System.out.println("  Please contact your HR Admin if you need access.");
            }
        }
        return false;
    }

    // ─── Session Management ───────────────────────────────────────────────────

    /** Logs out the current user and clears session */
    public static void logout() {
        System.out.println("\n  Goodbye, " + currentUsername + "! You have been logged out.");
        currentUsername = null;
        currentRole     = null;
        currentEmpID    = -1;
    }

    /** Returns true if current user is HR Admin */
    public static boolean isAdmin() {
        return "ADMIN".equals(currentRole);
    }

    /** Returns true if current user is a General Employee */
    public static boolean isEmployee() {
        return "EMPLOYEE".equals(currentRole);
    }

    /** Returns the currently logged in user's empID */
    public static int getCurrentEmpID() {
        return currentEmpID;
    }

    /** Returns the currently logged in user's role */
    public static String getCurrentRole() {
        return currentRole;
    }

    /** Returns the currently logged in username */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /** Returns true if someone is currently logged in */
    public static boolean isLoggedIn() {
        return currentUsername != null;
    }

    // ─── Password Hashing ────────────────────────────────────────────────────

    /**
     * Hashes a plain text password using SHA-256.
     * Returns the hex string of the hash.
     */
    public static String hashPassword(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plainText.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("  ERROR: Could not hash password: " + e.getMessage());
            return null;
        }
    }
}
