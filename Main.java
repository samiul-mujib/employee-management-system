import dao.DBConnection;
import util.AuthManager;
import ui.HRAdminMenu;
import ui.EmployeeMenu;
import java.util.Scanner;

/**
 * Main.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * Entry point for the Employee Management System.
 * Handles application startup, login, menu routing, and shutdown.
 *
 * Run this file to start the application.
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Welcome banner
        printBanner();

        // Run login loop — exits if max attempts reached
        boolean loggedIn = AuthManager.runLoginLoop(scanner);

        if (!loggedIn) {
            System.out.println("\n  Exiting system. Goodbye!");
            DBConnection.closeConnection();
            scanner.close();
            return;
        }

        // Route to correct menu based on role
        if (AuthManager.isAdmin()) {
            // HR Admin gets full access menu
            HRAdminMenu adminMenu = new HRAdminMenu(scanner);
            adminMenu.showMenu();

        } else if (AuthManager.isEmployee()) {
            // General Employee gets view-only menu
            EmployeeMenu empMenu = new EmployeeMenu(scanner);
            empMenu.showMenu();

        } else {
            System.out.println("  ERROR: Unknown role. Contact your system administrator.");
        }

        // Cleanup on exit
        AuthManager.logout();
        DBConnection.closeConnection();
        scanner.close();

        System.out.println("\n  Thank you for using the Employee Management System.");
        System.out.println("  Goodbye!\n");
    }

    /** Prints the application welcome banner */
    private static void printBanner() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║     EMPLOYEE MANAGEMENT SYSTEM               ║");
        System.out.println("║     Company Z - CSC3350 SP2026               ║");
        System.out.println("║     Team DataForge                           ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();
    }
}
