package util;

/**
 * InputValidator.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * Utility class that validates all user inputs before they are
 * sent to the database. All methods return boolean.
 *
 * Used by: HRAdminMenu.java, EmployeeMenu.java, MainMenu.java
 */
public class InputValidator {

    // Private constructor — no one should instantiate this class
    private InputValidator() {}

    /**
     * Returns true if the input is not null and not blank.
     * Use this for any required field.
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Returns true if the email contains @ and a dot after it.
     * Example valid: john@company.com
     * Example invalid: johnatcompany, john@, john@company
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) return false;
        String trimmed = email.trim();
        int atIndex = trimmed.indexOf('@');
        if (atIndex <= 0) return false;
        String domain = trimmed.substring(atIndex + 1);
        return domain.contains(".");
    }

    /**
     * Returns true if SSN matches format XXX-XX-XXXX.
     * Example valid: 123-45-6789
     * Example invalid: 123456789, 123-456-789
     */
    public static boolean isValidSSN(String ssn) {
        if (!isNotEmpty(ssn)) return false;
        return ssn.trim().matches("\\d{3}-\\d{2}-\\d{4}");
    }

    /**
     * Returns true if date matches format YYYY-MM-DD.
     * Example valid: 1990-03-15
     * Example invalid: 15/03/1990, 03-15-1990
     */
    public static boolean isValidDate(String date) {
        if (!isNotEmpty(date)) return false;
        return date.trim().matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /**
     * Returns true if the number is greater than 0.
     * Use this for salary, percentage, etc.
     */
    public static boolean isPositiveNumber(double num) {
        return num > 0;
    }

    /**
     * Returns true if the string is a valid positive integer.
     * Use this for empID input validation.
     */
    public static boolean isValidEmpID(String input) {
        if (!isNotEmpty(input)) return false;
        try {
            int id = Integer.parseInt(input.trim());
            return id > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns true if ZIP code is exactly 5 digits.
     * Example valid: 30301
     * Example invalid: 3030, ABCDE, 303011
     */
    public static boolean isValidZip(String zip) {
        if (!isNotEmpty(zip)) return false;
        return zip.trim().matches("\\d{5}");
    }

    /**
     * Returns true if phone number contains only digits,
     * spaces, dashes, or parentheses and is at least 10 chars.
     * Example valid: 4045550199, (404)555-0199, 404-555-0199
     */
    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) return false;
        String digits = phone.replaceAll("[\\s\\-().+]", "");
        return digits.matches("\\d{10,}");
    }

    /**
     * Returns true if input can be parsed as a valid double.
     * Use this for salary and percentage fields.
     */
    public static boolean isValidDouble(String input) {
        if (!isNotEmpty(input)) return false;
        try {
            Double.parseDouble(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns true if minSalary is strictly less than maxSalary.
     * Use this for the Update Salary by % feature.
     */
    public static boolean isValidSalaryRange(double min, double max) {
        return min > 0 && max > 0 && min < max;
    }
}
