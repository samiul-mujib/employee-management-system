package dao;

import model.Payroll;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PayrollDAO.java
 * CSc3350 Team Project SP2026 — Team DataForge
 * Author: Banhi
 *
 * Data Access Object for the payroll table.
 * Handles all database operations for payroll records:
 * get pay history, add new payroll record.
 *
 * All methods use PreparedStatements for security.
 * All methods use DBConnection.getConnection() for the DB connection.
 */
public class PayrollDAO {

    // ─── GET ──────────────────────────────────────────────────────────────────

    /**
     * Retrieves the complete pay statement history for a specific employee.
     * Results are sorted by most recent pay date first.
     *
     * @param empID the employee ID to get pay history for
     * @return List of Payroll objects sorted by pay_date descending
     */
    public List<Payroll> getPayHistory(int empID) {
        String sql = "SELECT * FROM payroll WHERE empid = ? ORDER BY pay_date DESC";
        List<Payroll> results = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, empID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                results.add(mapResultSet(rs));
            }
            rs.close();
            ps.close();

            if (results.isEmpty()) {
                System.out.println("  No payroll records found for employee ID: " + empID);
            }
            return results;

        } catch (SQLException ex) {
            System.out.println("  ERROR retrieving pay history: " + ex.getMessage());
            return results;
        }
    }

    // ─── ADD ──────────────────────────────────────────────────────────────────

    /**
     * Inserts a new payroll record into the payroll table.
     *
     * @param p Payroll object with all fields set
     * @return true if insert was successful, false if it failed
     */
    public boolean addPayroll(Payroll p) {
        String sql = "INSERT INTO payroll (payID, pay_date, earnings, fed_tax, fed_med, fed_SS, " +
                     "state_tax, retire_401k, health_care, empid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, p.getPayID());
            ps.setString(2, p.getPayDate());
            ps.setDouble(3, p.getEarnings());
            ps.setDouble(4, p.getFedTax());
            ps.setDouble(5, p.getFedMed());
            ps.setDouble(6, p.getFedSS());
            ps.setDouble(7, p.getStateTax());
            ps.setDouble(8, p.getRetire401k());
            ps.setDouble(9, p.getHealthCare());
            ps.setInt(10, p.getEmpid());

            int rows = ps.executeUpdate();
            ps.close();

            if (rows > 0) {
                System.out.println("  Payroll record added successfully.");
                return true;
            } else {
                System.out.println("  ERROR: Payroll record could not be added.");
                return false;
            }

        } catch (SQLException ex) {
            System.out.println("  ERROR adding payroll: " + ex.getMessage());
            return false;
        }
    }

    // ─── HELPER ───────────────────────────────────────────────────────────────

    /**
     * Maps a ResultSet row to a Payroll object.
     * Used internally by all retrieval methods.
     *
     * @param rs ResultSet positioned at the row to map
     * @return Payroll object with all fields populated
     */
    private Payroll mapResultSet(ResultSet rs) throws SQLException {
        return new Payroll(
            rs.getInt("payID"),
            rs.getString("pay_date"),
            rs.getDouble("earnings"),
            rs.getDouble("fed_tax"),
            rs.getDouble("fed_med"),
            rs.getDouble("fed_SS"),
            rs.getDouble("state_tax"),
            rs.getDouble("retire_401k"),
            rs.getDouble("health_care"),
            rs.getInt("empid")
        );
    }
}
