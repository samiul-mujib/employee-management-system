package model;

/**
 * Employee.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * Model class representing an Employee record from the employees table.
 * Fields match the database columns exactly.
 *
 * DB Table: employees
 * Columns: empid, Fname, Lname, email, HireDate, Salary, SSN, addressID
 */
public class Employee {

    // Fields matching DB columns
    private int    empid;
    private String fname;
    private String lname;
    private String email;
    private String hireDate;   // Stored as String, formatted as YYYY-MM-DD
    private double salary;
    private String ssn;
    private int    addressID;

    // ─── Constructors ────────────────────────────────────────────────────────

    /** Default constructor */
    public Employee() {}

    /** Full constructor — used when loading from DB */
    public Employee(int empid, String fname, String lname, String email,
                    String hireDate, double salary, String ssn, int addressID) {
        this.empid     = empid;
        this.fname     = fname;
        this.lname     = lname;
        this.email     = email;
        this.hireDate  = hireDate;
        this.salary    = salary;
        this.ssn       = ssn;
        this.addressID = addressID;
    }

    /** Constructor without empid — used when adding new employee (AUTO_INCREMENT) */
    public Employee(String fname, String lname, String email,
                    String hireDate, double salary, String ssn, int addressID) {
        this.fname     = fname;
        this.lname     = lname;
        this.email     = email;
        this.hireDate  = hireDate;
        this.salary    = salary;
        this.ssn       = ssn;
        this.addressID = addressID;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────

    public int    getEmpid()     { return empid; }
    public String getFname()     { return fname; }
    public String getLname()     { return lname; }
    public String getEmail()     { return email; }
    public String getHireDate()  { return hireDate; }
    public double getSalary()    { return salary; }
    public String getSsn()       { return ssn; }
    public int    getAddressID() { return addressID; }

    // ─── Setters ─────────────────────────────────────────────────────────────

    public void setEmpid(int empid)         { this.empid     = empid; }
    public void setFname(String fname)       { this.fname     = fname; }
    public void setLname(String lname)       { this.lname     = lname; }
    public void setEmail(String email)       { this.email     = email; }
    public void setHireDate(String hireDate) { this.hireDate  = hireDate; }
    public void setSalary(double salary)     { this.salary    = salary; }
    public void setSsn(String ssn)           { this.ssn       = ssn; }
    public void setAddressID(int addressID)  { this.addressID = addressID; }

    // ─── Utility ─────────────────────────────────────────────────────────────

    /** Returns full name */
    public String getFullName() {
        return fname + " " + lname;
    }

    /** Displays employee info in a formatted way */
    public void displayInfo() {
        System.out.println("========================================");
        System.out.println("  Employee ID   : " + empid);
        System.out.println("  Name          : " + getFullName());
        System.out.println("  Email         : " + email);
        System.out.println("  Hire Date     : " + hireDate);
        System.out.println("  Salary        : $" + String.format("%.2f", salary));
        System.out.println("  SSN           : " + ssn);
        System.out.println("  Address ID    : " + addressID);
        System.out.println("========================================");
    }

    /** toString for debugging */
    @Override
    public String toString() {
        return "Employee{" +
                "empid=" + empid +
                ", name='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                '}';
    }
}
