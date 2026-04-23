package ui;

import util.AuthManager;
import java.util.Scanner;

/**
 * MainMenu.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * The first screen the user sees after startup.
 * Handles login and routes to the correct menu based on user role.
 *
 * Called by Main.java after the login loop completes.
 * Routes ADMIN users to HRAdminMenu, EMPLOYEE users to EmployeeMenu.
 *
 * Requires: AuthManager (session management)
 */
public class MainMenu {

    private Scanner scanner;

    // ─── Constructor ─────────────────────────────────────────────────────────

    public MainMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    // ─── Entry Point ─────────────────────────────────────────────────────────

    /**
     * Runs the full login sequence and routes to the correct menu.
     * Exits the system if max login attempts are exceeded or role is unknown.
     */
    public void start() {
        printWelcomeBanner();

        // Run login — exits if max attempts reached
        boolean loggedIn = AuthManager.runLoginLoop(scanner);

        if (!loggedIn) {
            System.out.println("\n  Access denied. Exiting system.");
            return;
        }

        // Route to correct menu based on role
        routeToMenu();

        // Logout when menu returns (user chose logout)
        AuthManager.logout();
    }

    // ─── Routing ──────────────────────────────────────────────────────────────

    /**
     * Checks the logged-in user's role and opens the appropriate menu.
     */
    private void routeToMenu() {
        if (AuthManager.isAdmin()) {
            HRAdminMenu adminMenu = new HRAdminMenu(scanner);
            adminMenu.showMenu();

        } else if (AuthManager.isEmployee()) {
            EmployeeMenu empMenu = new EmployeeMenu(scanner);
            empMenu.showMenu();

        } else {
            System.out.println("  ERROR: Unknown role '" + AuthManager.getCurrentRole() + "'.");
            System.out.println("  Please contact your system administrator.");
        }
    }

    // ─── Display ──────────────────────────────────────────────────────────────

    /** Prints the welcome banner shown on first launch */
    private void printWelcomeBanner() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║     EMPLOYEE MANAGEMENT SYSTEM               ║");
        System.out.println("║     Company Z - CSC3350 SP2026               ║");
        System.out.println("║     Team DataForge                           ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  Please log in to continue.");
    }
}
