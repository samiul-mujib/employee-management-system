package model;

/**
 * Payroll.java
 * CSc3350 Team Project SP2026 — Team DataForge
 * Author: Banhi
 *
 * Model class representing a Payroll record from the payroll table.
 * Fields match the database columns exactly.
 *
 * DB Table: payroll
 * Columns: payID, pay_date, earnings, fed_tax, fed_med, fed_SS,
 *          state_tax, retire_401k, health_care, empid
 */
public class Payroll {

    // Fields matching DB columns
    private int    payID;
    private String payDate;
    private double earnings;
    private double fedTax;
    private double fedMed;
    private double fedSS;
    private double stateTax;
    private double retire401k;
    private double healthCare;
    private int    empid;

    // ─── Constructors ─────────────────────────────────────────────────────────

    /** Default constructor */
    public Payroll() {}

    /** Full constructor — used when loading from DB */
    public Payroll(int payID, String payDate, double earnings, double fedTax,
                   double fedMed, double fedSS, double stateTax,
                   double retire401k, double healthCare, int empid) {
        this.payID      = payID;
        this.payDate    = payDate;
        this.earnings   = earnings;
        this.fedTax     = fedTax;
        this.fedMed     = fedMed;
        this.fedSS      = fedSS;
        this.stateTax   = stateTax;
        this.retire401k = retire401k;
        this.healthCare = healthCare;
        this.empid      = empid;
    }

    /** Constructor without payID — used when adding new payroll record */
    public Payroll(String payDate, double earnings, double fedTax,
                   double fedMed, double fedSS, double stateTax,
                   double retire401k, double healthCare, int empid) {
        this.payDate    = payDate;
        this.earnings   = earnings;
        this.fedTax     = fedTax;
        this.fedMed     = fedMed;
        this.fedSS      = fedSS;
        this.stateTax   = stateTax;
        this.retire401k = retire401k;
        this.healthCare = healthCare;
        this.empid      = empid;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public int    getPayID()      { return payID; }
    public String getPayDate()    { return payDate; }
    public double getEarnings()   { return earnings; }
    public double getFedTax()     { return fedTax; }
    public double getFedMed()     { return fedMed; }
    public double getFedSS()      { return fedSS; }
    public double getStateTax()   { return stateTax; }
    public double getRetire401k() { return retire401k; }
    public double getHealthCare() { return healthCare; }
    public int    getEmpid()      { return empid; }

    // ─── Setters ──────────────────────────────────────────────────────────────

    public void setPayID(int payID)           { this.payID      = payID; }
    public void setPayDate(String payDate)     { this.payDate    = payDate; }
    public void setEarnings(double earnings)   { this.earnings   = earnings; }
    public void setFedTax(double fedTax)       { this.fedTax     = fedTax; }
    public void setFedMed(double fedMed)       { this.fedMed     = fedMed; }
    public void setFedSS(double fedSS)         { this.fedSS      = fedSS; }
    public void setStateTax(double stateTax)   { this.stateTax   = stateTax; }
    public void setRetire401k(double r401k)    { this.retire401k = r401k; }
    public void setHealthCare(double hc)       { this.healthCare = hc; }
    public void setEmpid(int empid)            { this.empid      = empid; }

    // ─── Utility ──────────────────────────────────────────────────────────────

    /**
     * Calculates and returns net pay.
     * Net Pay = Earnings - all deductions
     * Calculated in Java — not stored in the database.
     */
    public double getNetPay() {
        return earnings - fedTax - fedMed - fedSS - stateTax - retire401k - healthCare;
    }

    /**
     * Displays one payroll record in a formatted table row.
     */
    public void displayInfo() {
        System.out.println("========================================");
        System.out.println("  Pay ID       : " + payID);
        System.out.println("  Pay Date     : " + payDate);
        System.out.println("  Earnings     : $" + String.format("%.2f", earnings));
        System.out.println("  Fed Tax      : $" + String.format("%.2f", fedTax));
        System.out.println("  Medicare     : $" + String.format("%.2f", fedMed));
        System.out.println("  Social Sec   : $" + String.format("%.2f", fedSS));
        System.out.println("  State Tax    : $" + String.format("%.2f", stateTax));
        System.out.println("  401k         : $" + String.format("%.2f", retire401k));
        System.out.println("  Health Care  : $" + String.format("%.2f", healthCare));
        System.out.println("  Net Pay      : $" + String.format("%.2f", getNetPay()));
        System.out.println("========================================");
    }

    /** toString for debugging */
    @Override
    public String toString() {
        return "Payroll{" +
                "payID=" + payID +
                ", payDate='" + payDate + '\'' +
                ", earnings=" + earnings +
                ", netPay=" + getNetPay() +
                ", empid=" + empid +
                '}';
    }
}
