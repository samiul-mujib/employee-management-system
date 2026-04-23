package ui;

import util.AuthManager;
import dao.EmployeeDAO;
import dao.PayrollDAO;
import model.Employee;
import java.util.Scanner;
import ui.ReportMenu;

/**
 * EmployeeMenu.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * Console menu for General Employee users.
 * Provides view-only access — employees can view their own data
 * and pay statement history. Cannot edit any records.
 *
 * Requires: AuthManager (session), EmployeeDAO, PayrollDAO
 */
public class EmployeeMenu {

    private Scanner     scanner;
    private EmployeeDAO employeeDAO;
    private PayrollDAO  payrollDAO;

    // ─── Constructor ─────────────────────────────────────────────────────────

    public EmployeeMenu(Scanner scanner) {
        this.scanner     = scanner;
        this.employeeDAO = new EmployeeDAO();
        this.payrollDAO  = new PayrollDAO();
    }

    // ─── Main Menu Loop ───────────────────────────────────────────────────────

    /**
     * Displays the Employee menu and handles user selection.
     * Loops until the user chooses to logout.
     */
    public void showMenu() {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("  Enter choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    viewMyProfile();
                    break;
                case "2":
                    viewMyPayHistory();
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("  ERROR: Invalid option. Please enter 1, 2, or 3.");
            }
        }
    }

    // ─── Menu Options ─────────────────────────────────────────────────────────

    /**
     * Displays the current employee's own profile data.
     */
    private void viewMyProfile() {
        System.out.println("\n  Loading your profile...");
        int empID = AuthManager.getCurrentEmpID();

        try {
            Employee emp = employeeDAO.searchByEmpID(empID);
            if (emp != null) {
                System.out.println();
                emp.displayInfo();
            } else {
                System.out.println("  ERROR: Could not find your employee record. Contact HR Admin.");
            }
        } catch (Exception e) {
            System.out.println("  ERROR: Could not load profile: " + e.getMessage());
        }

        pause();
    }

    /**
     * Displays the current employee's pay statement history.
     */
    private void viewMyPayHistory() {
        System.out.println("\n  Loading your pay history...");
        int empID = AuthManager.getCurrentEmpID();
        try {
            ReportMenu reportMenu = new ReportMenu(scanner);
            reportMenu.showMenu();
        } catch (Exception e) {
            System.out.println("  ERROR: Could not load pay history: " + e.getMessage());
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /** Prints the Employee menu options */
    private void printMenu() {
        System.out.println("\n========================================");
        System.out.println("  Employee Menu: " + AuthManager.getCurrentUsername());
        System.out.println("========================================");
        System.out.println("  1. View My Profile");
        System.out.println("  2. View My Pay Statement History");
        System.out.println("  3. Logout");
        System.out.println("----------------------------------------");
    }

    /** Waits for user to press Enter before continuing */
    private void pause() {
        System.out.print("\n  Press Enter to continue...");
        scanner.nextLine();
    }
}
