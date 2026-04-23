package ui;

import dao.PayrollDAO;
import dao.DBConnection;
import model.Payroll;
import util.AuthManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * ReportMenu.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * Displays all reports for the Employee Management System.
 *
 * Report A: Pay statement history — General Employee (own records only)
 * Report B: Total pay for month by job title — HR Admin only
 * Report C: Total pay for month by division — HR Admin only
 * Report D: New employee hires within a date range — HR Admin only
 */
public class ReportMenu {

    private Scanner    scanner;
    private PayrollDAO payrollDAO;

    public ReportMenu(Scanner scanner) {
        this.scanner    = scanner;
        this.payrollDAO = new PayrollDAO();
    }

    // ─── Main Menu ────────────────────────────────────────────────────────────

    public void showMenu() {
        if (AuthManager.isAdmin()) {
            showAdminReportMenu();
        } else {
            showMyPayHistory();
        }
    }

    // ─── Admin Report Menu ────────────────────────────────────────────────────

    private void showAdminReportMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("           REPORTS MENU");
            System.out.println("========================================");
            System.out.println("  1. Employee Pay Statement History");
            System.out.println("  2. Total Pay for Month by Job Title");
            System.out.println("  3. Total Pay for Month by Division");
            System.out.println("  4. New Employee Hires by Date Range");
            System.out.println("  5. Back to Admin Menu");
            System.out.println("----------------------------------------");
            System.out.print("  Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": reportA_PayHistory();      break;
                case "2": reportB_PayByJobTitle();   break;
                case "3": reportC_PayByDivision();   break;
                case "4": reportD_NewHires();        break;
                case "5": running = false;           break;
                default:
                    System.out.println("  Invalid choice. Please enter 1-5.");
            }
        }
    }

    // ─── Report A: Pay Statement History ─────────────────────────────────────

    /**
     * HR Admin version — asks for empID first.
     */
    private void reportA_PayHistory() {
        System.out.println("\n  --- Pay Statement History ---");
        System.out.print("  Enter Employee ID: ");
        try {
            int empID = Integer.parseInt(scanner.nextLine().trim());
            showPayHistory(empID);
        } catch (NumberFormatException e) {
            System.out.println("  ERROR: Invalid Employee ID.");
        }
    }

    /**
     * Employee version — uses logged in empID automatically.
     */
    private void showMyPayHistory() {
        int empID = AuthManager.getCurrentEmpID();
        showPayHistory(empID);
    }

    /**
     * Core pay history display — used by both admin and employee versions.
     */
    private void showPayHistory(int empID) {
        List<Payroll> records = payrollDAO.getPayHistory(empID);

        if (records == null || records.isEmpty()) {
            System.out.println("  No pay records found for Employee ID: " + empID);
            pause();
            return;
        }

        System.out.println("\n  Pay Statement History - Employee ID: " + empID);
        System.out.println();
        System.out.println("  " + "-".repeat(112));
        System.out.printf("  %-12s %-12s %-10s %-10s %-10s %-10s %-10s %-10s %-12s%n",
                "Pay Date", "Earnings", "Fed Tax", "Medicare",
                "Soc Sec", "State Tax", "401k", "Health", "Net Pay");
        System.out.println("  " + "-".repeat(112));

        double totalEarnings = 0;
        double totalNetPay   = 0;

        for (Payroll p : records) {
            System.out.printf("  %-12s $%-11.2f $%-9.2f $%-9.2f $%-9.2f $%-9.2f $%-9.2f $%-9.2f $%-11.2f%n",
                    p.getPayDate(),
                    p.getEarnings(),
                    p.getFedTax(),
                    p.getFedMed(),
                    p.getFedSS(),
                    p.getStateTax(),
                    p.getRetire401k(),
                    p.getHealthCare(),
                    p.getNetPay());
            totalEarnings += p.getEarnings();
            totalNetPay   += p.getNetPay();
        }

        System.out.println("  " + "-".repeat(112));
        System.out.println();
        System.out.printf("  Total Records  : %d%n", records.size());
        System.out.printf("  Total Earnings : $%.2f%n", totalEarnings);
        System.out.printf("  Total Net Pay  : $%.2f%n", totalNetPay);

        pause();
    }

    // ─── Report B: Total Pay by Job Title ────────────────────────────────────

    private void reportB_PayByJobTitle() {
        System.out.println("\n  --- Total Pay for Month by Job Title ---");
        System.out.print("  Enter month (YYYY-MM): ");
        String month = scanner.nextLine().trim();

        if (!month.matches("\\d{4}-\\d{2}")) {
            System.out.println("  ERROR: Month must be in format YYYY-MM (e.g. 2026-01)");
            pause();
            return;
        }

        String sql = "SELECT jt.job_title, " +
                     "SUM(p.earnings) AS total_pay, " +
                     "COUNT(p.empid) AS employee_count " +
                     "FROM payroll p " +
                     "JOIN employee_job_titles ejt ON p.empid = ejt.empid " +
                     "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                     "WHERE DATE_FORMAT(p.pay_date, '%Y-%m') = ? " +
                     "GROUP BY jt.job_title " +
                     "ORDER BY total_pay DESC";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, month);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n  Total Pay by Job Title - Month: " + month);
            System.out.println();
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-30s %-15s %-10s%n", "Job Title", "Total Pay", "Employees");
            System.out.println("  " + "-".repeat(60));

            boolean hasResults = false;
            double grandTotal = 0;

            while (rs.next()) {
                hasResults = true;
                String jobTitle     = rs.getString("job_title");
                double totalPay     = rs.getDouble("total_pay");
                int    empCount     = rs.getInt("employee_count");
                grandTotal         += totalPay;

                System.out.printf("  %-30s $%-14.2f %-10d%n",
                        jobTitle, totalPay, empCount);
            }

            if (!hasResults) {
                System.out.println("  No payroll records found for month: " + month);
            } else {
                System.out.println("  " + "-".repeat(60));
                System.out.printf("  %-30s $%-14.2f%n", "GRAND TOTAL", grandTotal);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("  ERROR: Could not load report: " + e.getMessage());
        }

        pause();
    }

    // ─── Report C: Total Pay by Division ─────────────────────────────────────

    private void reportC_PayByDivision() {
        System.out.println("\n  --- Total Pay for Month by Division ---");
        System.out.print("  Enter month (YYYY-MM): ");
        String month = scanner.nextLine().trim();

        if (!month.matches("\\d{4}-\\d{2}")) {
            System.out.println("  ERROR: Month must be in format YYYY-MM (e.g. 2026-01)");
            pause();
            return;
        }

        String sql = "SELECT d.Name AS division_name, " +
                     "SUM(p.earnings) AS total_pay, " +
                     "COUNT(p.empid) AS employee_count " +
                     "FROM payroll p " +
                     "JOIN employee_division ed ON p.empid = ed.empid " +
                     "JOIN division d ON ed.div_ID = d.ID " +
                     "WHERE DATE_FORMAT(p.pay_date, '%Y-%m') = ? " +
                     "GROUP BY d.Name " +
                     "ORDER BY total_pay DESC";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, month);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n  Total Pay by Division - Month: " + month);
            System.out.println();
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-25s %-15s %-10s%n", "Division", "Total Pay", "Employees");
            System.out.println("  " + "-".repeat(60));

            boolean hasResults = false;
            double grandTotal  = 0;

            while (rs.next()) {
                hasResults       = true;
                String divName   = rs.getString("division_name");
                double totalPay  = rs.getDouble("total_pay");
                int    empCount  = rs.getInt("employee_count");
                grandTotal      += totalPay;

                System.out.printf("  %-25s $%-14.2f %-10d%n",
                        divName, totalPay, empCount);
            }

            if (!hasResults) {
                System.out.println("  No payroll records found for month: " + month);
            } else {
                System.out.println("  " + "-".repeat(60));
                System.out.printf("  %-25s $%-14.2f%n", "GRAND TOTAL", grandTotal);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("  ERROR: Could not load report: " + e.getMessage());
        }

        pause();
    }

    // ─── Report D: New Hires by Date Range ───────────────────────────────────

    private void reportD_NewHires() {
        System.out.println("\n  --- New Employee Hires by Date Range ---");
        System.out.print("  Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine().trim();

        if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("  ERROR: Date must be in format YYYY-MM-DD");
            pause();
            return;
        }

        System.out.print("  Enter end date   (YYYY-MM-DD): ");
        String endDate = scanner.nextLine().trim();

        if (!endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("  ERROR: Date must be in format YYYY-MM-DD");
            pause();
            return;
        }

        String sql = "SELECT empid, Fname, Lname, email, HireDate, Salary " +
                     "FROM employees " +
                     "WHERE HireDate BETWEEN ? AND ? " +
                     "ORDER BY HireDate DESC";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n  New Hires from " + startDate + " to " + endDate);
            System.out.println();
            System.out.println("  " + "-".repeat(80));
            System.out.printf("  %-6s %-20s %-30s %-12s%n",
                    "ID", "Name", "Email", "Hire Date");
            System.out.println("  " + "-".repeat(80));

            boolean hasResults = false;
            int count = 0;

            while (rs.next()) {
                hasResults    = true;
                int    empID  = rs.getInt("empid");
                String fname  = rs.getString("Fname");
                String lname  = rs.getString("Lname");
                String email  = rs.getString("email");
                String hire   = rs.getString("HireDate");
                count++;

                System.out.printf("  %-6d %-20s %-30s %-12s%n",
                        empID, fname + " " + lname, email, hire);
            }

            if (!hasResults) {
                System.out.println("  No new hires found in that date range.");
            } else {
                System.out.println("  " + "-".repeat(80));
                System.out.println("  Total New Hires: " + count);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("  ERROR: Could not load report: " + e.getMessage());
        }

        pause();
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    private void pause() {
        System.out.println();
        System.out.print("  Press Enter to continue...");
        scanner.nextLine();
    }
}