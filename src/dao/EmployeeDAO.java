package dao;

import model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAO.java
 * CSc3350 Team Project SP2026 — Team DataForge
 * Author: Manna
 *
 * Data Access Object for the employees table.
 * Handles all database operations for employees:
 * add, search, update, delete, salary update.
 *
 * All methods use PreparedStatements for security.
 * All methods use DBConnection.getConnection() for the DB connection.
 */
public class EmployeeDAO {

    // ─── ADD ──────────────────────────────────────────────────────────────────

    /**
     * Inserts a new employee into the employees table.
     * empID is provided manually (not AUTO_INCREMENT in this schema).
     *
     * @param e Employee object with all fields set
     * @return true if insert was successful, false if it failed
     */
    public boolean addEmployee(Employee e) {
        String sql = "INSERT INTO employees (Fname, Lname, email, HireDate, Salary, SSN, addressID) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, 
                PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getFname());
            ps.setString(2, e.getLname());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getHireDate());
            ps.setDouble(5, e.getSalary());
            ps.setString(6, e.getSsn());
            ps.setInt(7, e.getAddressID());

            int rows = ps.executeUpdate();

            // Get the AUTO_INCREMENT generated empID back
            if (rows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    e.setEmpid(generatedKeys.getInt(1));
                }
                generatedKeys.close();
                System.out.println("  Employee added successfully! Assigned empID: " + e.getEmpid());
            }

            ps.close();
            return rows > 0;

        } catch (SQLException ex) {
            System.out.println("  ERROR adding employee: " + ex.getMessage());
            return false;
        }
    }

    // ─── SEARCH ───────────────────────────────────────────────────────────────

    /**
     * Searches for an employee by their empID.
     *
     * @param id the employee ID to search for
     * @return Employee object if found, null if not found
     */
    public Employee searchByEmpID(int id) {
        String sql = "SELECT * FROM employees WHERE empid = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee e = mapResultSet(rs);
                rs.close();
                ps.close();
                return e;
            } else {
                System.out.println("  No employee found with ID: " + id);
                rs.close();
                ps.close();
                return null;
            }

        } catch (SQLException ex) {
            System.out.println("  ERROR searching by empID: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Searches for employees by last name (case-insensitive).
     *
     * @param lname the last name to search for
     * @return List of matching Employee objects, empty list if none found
     */
    public List<Employee> searchByName(String lname) {
        String sql = "SELECT * FROM employees WHERE LOWER(Lname) = ?";
        List<Employee> results = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, lname.toLowerCase().trim());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                results.add(mapResultSet(rs));
            }
            rs.close();
            ps.close();

            if (results.isEmpty()) {
                System.out.println("  No employee found with last name: " + lname);
            }
            return results;

        } catch (SQLException ex) {
            System.out.println("  ERROR searching by name: " + ex.getMessage());
            return results;
        }
    }

    /**
     * Searches for an employee by SSN.
     *
     * @param ssn the SSN to search for (format: XXX-XX-XXXX)
     * @return Employee object if found, null if not found
     */
    public Employee searchBySSN(String ssn) {
        String sql = "SELECT * FROM employees WHERE SSN = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ssn.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee e = mapResultSet(rs);
                rs.close();
                ps.close();
                return e;
            } else {
                System.out.println("  No employee found with SSN: " + ssn);
                rs.close();
                ps.close();
                return null;
            }

        } catch (SQLException ex) {
            System.out.println("  ERROR searching by SSN: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Searches for employees by date of birth.
     *
     * @param dob the date of birth to search for (format: YYYY-MM-DD)
     * @return List of matching Employee objects, empty list if none found
     */
    public List<Employee> searchByDOB(String dob) {
        String sql = "SELECT e.* FROM employees e " +
                     "JOIN addresses a ON e.addressID = a.addressID " +
                     "WHERE a.DOB = ?";
        List<Employee> results = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dob.trim());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                results.add(mapResultSet(rs));
            }
            rs.close();
            ps.close();

            if (results.isEmpty()) {
                System.out.println("  No employee found with DOB: " + dob);
            }
            return results;

        } catch (SQLException ex) {
            System.out.println("  ERROR searching by DOB: " + ex.getMessage());
            return results;
        }
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return List of all Employee objects
     */
    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM employees ORDER BY empid";
        List<Employee> results = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                results.add(mapResultSet(rs));
            }
            rs.close();
            ps.close();
            return results;

        } catch (SQLException ex) {
            System.out.println("  ERROR retrieving all employees: " + ex.getMessage());
            return results;
        }
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    /**
     * Updates an existing employee record in the database.
     * Uses the empid field to identify which record to update.
     *
     * @param e Employee object with updated field values
     * @return true if update was successful, false if it failed
     */
    public boolean updateEmployee(Employee e) {
        String sql = "UPDATE employees SET Fname=?, Lname=?, email=?, HireDate=?, Salary=?, SSN=?, addressID=? " +
                     "WHERE empid=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, e.getFname());
            ps.setString(2, e.getLname());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getHireDate());
            ps.setDouble(5, e.getSalary());
            ps.setString(6, e.getSsn());
            ps.setInt(7, e.getAddressID());
            ps.setInt(8, e.getEmpid());

            int rows = ps.executeUpdate();
            ps.close();

            if (rows > 0) {
                System.out.println("  Employee record updated successfully.");
                return true;
            } else {
                System.out.println("  ERROR: No employee found with ID: " + e.getEmpid());
                return false;
            }

        } catch (SQLException ex) {
            System.out.println("  ERROR updating employee: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Updates salary for all employees whose current salary falls
     * within the specified range by the given percentage.
     *
     * @param min minimum salary of the range
     * @param max maximum salary of the range
     * @param pct percentage increase to apply (e.g. 10 for 10%)
     * @return number of employee records updated
     */
    public int updateSalaryByRange(double min, double max, double pct) {
        String sql = "UPDATE employees SET Salary = Salary * (1 + ? / 100) WHERE Salary BETWEEN ? AND ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, pct);
            ps.setDouble(2, min);
            ps.setDouble(3, max);

            int rows = ps.executeUpdate();
            ps.close();

            if (rows > 0) {
                System.out.println("  " + rows + " employee(s) salary updated successfully.");
            } else {
                System.out.println("  No employees found within the specified salary range.");
            }
            return rows;

        } catch (SQLException ex) {
            System.out.println("  ERROR updating salary: " + ex.getMessage());
            return 0;
        }
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Deletes an employee and all related records from the database.
     * Deletes in this order to avoid FK constraint errors:
     * 1. payroll
     * 2. employee_job_titles
     * 3. employee_division
     * 4. employees
     *
     * @param id the empID of the employee to delete
     * @return true if deletion was successful, false if it failed
     */
    public boolean deleteEmployee(int id) {
        Connection conn = DBConnection.getConnection();
        try {
            // Turn off auto-commit for transaction
            conn.setAutoCommit(false);

            // Step 1: Delete payroll records
            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM payroll WHERE empid = ?");
            ps1.setInt(1, id);
            ps1.executeUpdate();
            ps1.close();

            // Step 2: Delete job title assignments
            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM employee_job_titles WHERE empid = ?");
            ps2.setInt(1, id);
            ps2.executeUpdate();
            ps2.close();

            // Step 3: Delete division assignment
            PreparedStatement ps3 = conn.prepareStatement("DELETE FROM employee_division WHERE empid = ?");
            ps3.setInt(1, id);
            ps3.executeUpdate();
            ps3.close();

            // Step 4: Delete the employee record itself
            PreparedStatement ps4 = conn.prepareStatement("DELETE FROM employees WHERE empid = ?");
            ps4.setInt(1, id);
            int rows = ps4.executeUpdate();
            ps4.close();

            // Commit the transaction
            conn.commit();
            conn.setAutoCommit(true);

            if (rows > 0) {
                System.out.println("  Employee deleted successfully.");
                return true;
            } else {
                System.out.println("  ERROR: No employee found with ID: " + id);
                return false;
            }

        } catch (SQLException ex) {
            // Rollback if anything goes wrong
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                System.out.println("  Transaction rolled back. No changes were made.");
            } catch (SQLException rollbackEx) {
                System.out.println("  ERROR during rollback: " + rollbackEx.getMessage());
            }
            System.out.println("  ERROR deleting employee: " + ex.getMessage());
            return false;
        }
    }

    // ─── HELPER ───────────────────────────────────────────────────────────────

    /**
     * Maps a ResultSet row to an Employee object.
     * Used internally by all search methods.
     *
     * @param rs ResultSet positioned at the row to map
     * @return Employee object with all fields populated
     */
    private Employee mapResultSet(ResultSet rs) throws SQLException {
        return new Employee(
            rs.getInt("empid"),
            rs.getString("Fname"),
            rs.getString("Lname"),
            rs.getString("email"),
            rs.getString("HireDate"),
            rs.getDouble("Salary"),
            rs.getString("SSN"),
            rs.getInt("addressID")
        );
    }
}
