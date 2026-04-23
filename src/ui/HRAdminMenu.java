package ui;

import util.AuthManager;
import util.InputValidator;
import dao.EmployeeDAO;
import dao.AddressDAO;
import model.Employee;
import model.Address;
import java.util.List;
import java.util.Scanner;

/**
 * HRAdminMenu.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * Console menu for HR Admin users.
 * All options have a "0. Back" option to return to the main menu.
 */
public class HRAdminMenu {

    private Scanner     scanner;
    private EmployeeDAO employeeDAO;
    private AddressDAO  addressDAO;

    public HRAdminMenu(Scanner scanner) {
        this.scanner     = scanner;
        this.employeeDAO = new EmployeeDAO();
        this.addressDAO  = new AddressDAO();
    }

    // ─── Main Menu Loop ───────────────────────────────────────────────────────

    public void showMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("  Enter choice: ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": addNewEmployee();       break;
                case "2": searchEmployee();       break;
                case "3": updateEmployee();       break;
                case "4": deleteEmployee();       break;
                case "5": updateSalaryByPercent(); break;
                case "6": viewReports();          break;
                case "7": running = false;        break;
                default:
                    System.out.println("  ERROR: Invalid option. Please enter 1-7.");
            }
        }
    }

    // ─── Option 1: Add New Employee ───────────────────────────────────────────

    private void addNewEmployee() {
        System.out.println("\n========================================");
        System.out.println("  Add New Employee");
        System.out.println("  (Type 0 at any prompt to go back)");
        System.out.println("========================================");

        // --- Address fields ---
        System.out.println("  Enter Address Details:");

        String street = promptNonEmptyOrBack("  Street: ");
        if (street == null) return;

        System.out.print("  City ID (0 to go back): ");
        String cityInput = scanner.nextLine().trim();
        if (cityInput.equals("0")) return;
        int cityID;
        try { cityID = Integer.parseInt(cityInput); }
        catch (NumberFormatException e) { System.out.println("  ERROR: Invalid City ID."); pause(); return; }

        System.out.print("  State ID (0 to go back): ");
        String stateInput = scanner.nextLine().trim();
        if (stateInput.equals("0")) return;
        int stateID;
        try { stateID = Integer.parseInt(stateInput); }
        catch (NumberFormatException e) { System.out.println("  ERROR: Invalid State ID."); pause(); return; }

        String zip = promptNonEmptyOrBack("  ZIP Code: ");
        if (zip == null) return;

        String dob = promptValidDateOrBack("  Date of Birth (YYYY-MM-DD): ");
        if (dob == null) return;

        String phone = promptNonEmptyOrBack("  Phone Number: ");
        if (phone == null) return;

        String ecName = promptNonEmptyOrBack("  Emergency Contact Name: ");
        if (ecName == null) return;

        String ecPhone = promptNonEmptyOrBack("  Emergency Contact Phone: ");
        if (ecPhone == null) return;

        // Insert address first to get addressID
        Address address = new Address(street, cityID, stateID, zip, dob, phone, ecName, ecPhone);
        int addressID = -1;
        try {
            addressID = addressDAO.addAddress(address);
        } catch (Exception e) {
            System.out.println("  ERROR: Could not save address: " + e.getMessage());
            pause(); return;
        }

        if (addressID <= 0) {
            System.out.println("  ERROR: Address insert failed. Employee was not added.");
            pause(); return;
        }

        // --- Employee fields ---
        System.out.println("\n  Enter Employee Details:");

        String fname = promptNonEmptyOrBack("  First Name: ");
        if (fname == null) return;

        String lname = promptNonEmptyOrBack("  Last Name: ");
        if (lname == null) return;

        String email = promptValidEmailOrBack("  Email: ");
        if (email == null) return;

        String hireDate = promptValidDateOrBack("  Hire Date (YYYY-MM-DD): ");
        if (hireDate == null) return;

        Double salary = promptPositiveDoubleOrBack("  Salary: ");
        if (salary == null) return;

        String ssn = promptValidSSNOrBack("  SSN (XXX-XX-XXXX): ");
        if (ssn == null) return;

        Employee emp = new Employee(fname, lname, email, hireDate, salary, ssn, addressID);
        try {
            employeeDAO.addEmployee(emp);
            System.out.println("\n  Employee added successfully! Assigned empID: " + emp.getEmpid());
        } catch (Exception e) {
            System.out.println("  ERROR: Could not add employee: " + e.getMessage());
        }

        pause();
    }

    // ─── Option 2: Search Employee ────────────────────────────────────────────

    private void searchEmployee() {
        boolean searching = true;
        while (searching) {
            System.out.println("\n========================================");
            System.out.println("  Search Employee");
            System.out.println("========================================");
            System.out.println("  1. Search by Employee ID");
            System.out.println("  2. Search by Last Name");
            System.out.println("  3. Search by SSN");
            System.out.println("  4. Search by Date of Birth");
            System.out.println("  0. Back to Main Menu");
            System.out.println("----------------------------------------");
            System.out.print("  Enter choice: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) return;

            Employee result = null;

            try {
                switch (choice) {
                    case "1":
                        System.out.print("  Enter Employee ID (0 to go back): ");
                        String idStr = scanner.nextLine().trim();
                        if (idStr.equals("0")) break;
                        int id = Integer.parseInt(idStr);
                        result = employeeDAO.searchByEmpID(id);
                        if (result != null) { System.out.println(); result.displayInfo(); }
                        else System.out.println("  No employee found with ID: " + id);
                        pause();
                        break;

                    case "2":
                        String lname = promptNonEmptyOrBack("  Enter Last Name: ");
                        if (lname == null) break;
                        List<Employee> byName = employeeDAO.searchByName(lname);
                        if (byName == null || byName.isEmpty()) {
                            System.out.println("  No employee found with last name: " + lname);
                        } else {
                            for (Employee e : byName) e.displayInfo();
                        }
                        pause();
                        break;

                    case "3":
                        String ssn = promptValidSSNOrBack("  Enter SSN (XXX-XX-XXXX): ");
                        if (ssn == null) break;
                        result = employeeDAO.searchBySSN(ssn);
                        if (result != null) { System.out.println(); result.displayInfo(); }
                        else System.out.println("  No employee found with that SSN.");
                        pause();
                        break;

                    case "4":
                        String dob = promptValidDateOrBack("  Enter DOB (YYYY-MM-DD): ");
                        if (dob == null) break;
                        List<Employee> byDOB = employeeDAO.searchByDOB(dob);
                        if (byDOB == null || byDOB.isEmpty()) {
                            System.out.println("  No employee found with DOB: " + dob);
                        } else {
                            for (Employee e : byDOB) e.displayInfo();
                        }
                        pause();
                        break;

                    default:
                        System.out.println("  ERROR: Invalid choice. Enter 0-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("  ERROR: Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("  ERROR: Search failed: " + e.getMessage());
            }
        }
    }

    // ─── Option 3: Update Employee ────────────────────────────────────────────

    private void updateEmployee() {
        System.out.println("\n========================================");
        System.out.println("  Update Employee");
        System.out.println("  (Type 0 to go back)");
        System.out.println("========================================");

        System.out.print("  Enter Employee ID to update (0 to go back): ");
        String idStr = scanner.nextLine().trim();
        if (idStr.equals("0")) return;

        int id;
        try { id = Integer.parseInt(idStr); }
        catch (NumberFormatException e) { System.out.println("  ERROR: Invalid ID."); pause(); return; }

        Employee emp = null;
        try {
            emp = employeeDAO.searchByEmpID(id);
        } catch (Exception e) {
            System.out.println("  ERROR: " + e.getMessage());
            pause(); return;
        }

        if (emp == null) {
            System.out.println("  ERROR: No employee found with ID " + id);
            pause(); return;
        }

        System.out.println("\n  Current data:");
        emp.displayInfo();
        System.out.println("\n  Enter new values (press Enter to keep current, type 0 to go back):");

        // First Name
        System.out.print("  First Name [" + emp.getFname() + "]: ");
        String fname = scanner.nextLine().trim();
        if (fname.equals("0")) return;
        if (!fname.isEmpty()) emp.setFname(fname);

        // Last Name
        System.out.print("  Last Name [" + emp.getLname() + "]: ");
        String lname = scanner.nextLine().trim();
        if (lname.equals("0")) return;
        if (!lname.isEmpty()) emp.setLname(lname);

        // Email
        System.out.print("  Email [" + emp.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (email.equals("0")) return;
        if (!email.isEmpty()) {
            if (InputValidator.isValidEmail(email)) emp.setEmail(email);
            else System.out.println("  WARNING: Invalid email — keeping original.");
        }

        // Hire Date
        System.out.print("  Hire Date [" + emp.getHireDate() + "]: ");
        String hireDate = scanner.nextLine().trim();
        if (hireDate.equals("0")) return;
        if (!hireDate.isEmpty()) {
            if (InputValidator.isValidDate(hireDate)) emp.setHireDate(hireDate);
            else System.out.println("  WARNING: Invalid date — keeping original.");
        }

        // Salary
        System.out.print("  Salary [" + emp.getSalary() + "]: ");
        String salaryStr = scanner.nextLine().trim();
        if (salaryStr.equals("0")) return;
        if (!salaryStr.isEmpty()) {
            try {
                double salary = Double.parseDouble(salaryStr);
                if (InputValidator.isPositiveNumber(salary)) emp.setSalary(salary);
                else System.out.println("  WARNING: Salary must be positive — keeping original.");
            } catch (NumberFormatException e) {
                System.out.println("  WARNING: Invalid number — keeping original salary.");
            }
        }

        try {
            employeeDAO.updateEmployee(emp);
            System.out.println("\n  Employee updated successfully!");
        } catch (Exception e) {
            System.out.println("  ERROR: Could not update employee: " + e.getMessage());
        }

        pause();
    }

    // ─── Option 4: Delete Employee ────────────────────────────────────────────

    private void deleteEmployee() {
        System.out.println("\n========================================");
        System.out.println("  Delete Employee");
        System.out.println("  (Type 0 to go back)");
        System.out.println("========================================");

        System.out.print("  Enter Employee ID to delete (0 to go back): ");
        String idStr = scanner.nextLine().trim();
        if (idStr.equals("0")) return;

        int id;
        try { id = Integer.parseInt(idStr); }
        catch (NumberFormatException e) { System.out.println("  ERROR: Invalid ID."); pause(); return; }

        Employee emp = null;
        try {
            emp = employeeDAO.searchByEmpID(id);
        } catch (Exception e) {
            System.out.println("  ERROR: " + e.getMessage());
            pause(); return;
        }

        if (emp == null) {
            System.out.println("  ERROR: No employee found with ID " + id);
            pause(); return;
        }

        System.out.println("\n  About to delete:");
        emp.displayInfo();
        System.out.print("\n  Are you sure? This cannot be undone. (yes / no / 0 to go back): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("0")) return;

        if (confirm.equals("yes")) {
            try {
                employeeDAO.deleteEmployee(id);
                System.out.println("\n  Employee deleted successfully.");
            } catch (Exception e) {
                System.out.println("  ERROR: Could not delete employee: " + e.getMessage());
            }
        } else {
            System.out.println("  Deletion cancelled. Employee record remains unchanged.");
        }

        pause();
    }

    // ─── Option 5: Update Salary by % ─────────────────────────────────────────

    private void updateSalaryByPercent() {
        System.out.println("\n========================================");
        System.out.println("  Update Salary by Percentage");
        System.out.println("  (Type 0 at any prompt to go back)");
        System.out.println("========================================");

        Double min = promptPositiveDoubleOrBack("  Minimum current salary (0 to go back): $");
        if (min == null) return;

        Double max = promptPositiveDoubleOrBack("  Maximum current salary (0 to go back): $");
        if (max == null) return;

        if (min >= max) {
            System.out.println("  ERROR: Minimum salary must be less than maximum salary.");
            pause(); return;
        }

        System.out.print("  Percentage increase (e.g. 5 for 5%, 0 to go back): ");
        String pctStr = scanner.nextLine().trim();
        if (pctStr.equals("0")) return;

        double pct;
        try {
            pct = Double.parseDouble(pctStr);
            if (pct <= 0) {
                System.out.println("  ERROR: Percentage must be greater than 0.");
                pause(); return;
            }
        } catch (NumberFormatException e) {
            System.out.println("  ERROR: Invalid number.");
            pause(); return;
        }

        System.out.printf("  Applying %.1f%% raise to employees earning $%.2f - $%.2f.%n", pct, min, max);
        System.out.print("  Confirm? (yes / no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            try {
                int updated = employeeDAO.updateSalaryByRange(min, max, pct);
                System.out.println("\n  " + updated + " employee(s) salary updated successfully!");
            } catch (Exception e) {
                System.out.println("  ERROR: Could not update salaries: " + e.getMessage());
            }
        } else {
            System.out.println("  Salary update cancelled.");
        }

        pause();
    }

    // ─── Option 6: View Reports ───────────────────────────────────────────────

    private void viewReports() {
        boolean inReports = true;
        while (inReports) {
            System.out.println("\n========================================");
            System.out.println("  Reports");
            System.out.println("========================================");
            System.out.println("  1. View All Employees");
            System.out.println("  2. Pay Statement Reports");
            System.out.println("  0. Back to Main Menu");
            System.out.println("----------------------------------------");
            System.out.print("  Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    try {
                        List<Employee> all = employeeDAO.getAllEmployees();
                        if (all == null || all.isEmpty()) {
                            System.out.println("  No employees found.");
                        } else {
                            System.out.println("\n  All Employees (" + all.size() + " total):");
                            System.out.println("  " + "-".repeat(60));
                            for (Employee e : all) {
                                System.out.printf("  [%d] %-25s %s%n",
                                        e.getEmpid(), e.getFullName(), e.getEmail());
                            }
                            System.out.println("  " + "-".repeat(60));
                        }
                    } catch (Exception e) {
                        System.out.println("  ERROR: " + e.getMessage());
                    }
                    pause();
                    break;
                case "2":
                    ReportMenu reportMenu = new ReportMenu(scanner);
                    reportMenu.showMenu();
                    break;
                case "0":
                    inReports = false;
                    break;
                default:
                    System.out.println("  ERROR: Invalid choice. Enter 0, 1, or 2.");
            }
        }
    }

    // ─── Print Menu ───────────────────────────────────────────────────────────

    private void printMenu() {
        System.out.println("\n========================================");
        System.out.println("  HR Admin Menu | " + AuthManager.getCurrentUsername());
        System.out.println("========================================");
        System.out.println("  1. Add New Employee");
        System.out.println("  2. Search Employee");
        System.out.println("  3. Update Employee Data");
        System.out.println("  4. Delete Employee");
        System.out.println("  5. Update Salary by %");
        System.out.println("  6. View Reports");
        System.out.println("  7. Logout");
        System.out.println("----------------------------------------");
    }

    // ─── Prompt Helpers with Back (return null = user typed 0) ───────────────

    private String promptNonEmptyOrBack(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (value.equals("0")) return null;
            if (InputValidator.isNotEmpty(value)) return value;
            System.out.println("  ERROR: This field cannot be empty. Type 0 to go back.");
        }
    }

    private String promptValidEmailOrBack(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (value.equals("0")) return null;
            if (InputValidator.isValidEmail(value)) return value;
            System.out.println("  ERROR: Invalid email. Must be user@domain.com. Type 0 to go back.");
        }
    }

    private String promptValidSSNOrBack(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (value.equals("0")) return null;
            if (InputValidator.isValidSSN(value)) return value;
            System.out.println("  ERROR: Invalid SSN. Must be XXX-XX-XXXX. Type 0 to go back.");
        }
    }

    private String promptValidDateOrBack(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (value.equals("0")) return null;
            if (InputValidator.isValidDate(value)) return value;
            System.out.println("  ERROR: Invalid date. Must be YYYY-MM-DD. Type 0 to go back.");
        }
    }

    private Double promptPositiveDoubleOrBack(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equals("0")) return null;
            try {
                double value = Double.parseDouble(input);
                if (InputValidator.isPositiveNumber(value)) return value;
                System.out.println("  ERROR: Value must be greater than 0. Type 0 to go back.");
            } catch (NumberFormatException e) {
                System.out.println("  ERROR: Please enter a valid number. Type 0 to go back.");
            }
        }
    }

    private void pause() {
        System.out.print("\n  Press Enter to continue...");
        scanner.nextLine();
    }
}