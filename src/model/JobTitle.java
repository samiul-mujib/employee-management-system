package model;

/**
 * JobTitle.java
 * CSc3350 Team Project SP2026 — Team DataForge
 * Author: Manna
 *
 * Model class representing a JobTitle record from the job_titles table.
 * Fields match the database columns exactly.
 *
 * DB Table: job_titles
 * Columns: job_title_id, job_title
 */
public class JobTitle {

    // Fields matching DB columns
    private int    jobTitleId;
    private String jobTitle;

    // ─── Constructors ─────────────────────────────────────────────────────────

    /** Default constructor */
    public JobTitle() {}

    /** Full constructor — used when loading from DB */
    public JobTitle(int jobTitleId, String jobTitle) {
        this.jobTitleId = jobTitleId;
        this.jobTitle   = jobTitle;
    }

    /** Constructor without ID — used when adding new job title */
    public JobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public int    getJobTitleId() { return jobTitleId; }
    public String getJobTitle()   { return jobTitle; }

    // ─── Setters ──────────────────────────────────────────────────────────────

    public void setJobTitleId(int jobTitleId) { this.jobTitleId = jobTitleId; }
    public void setJobTitle(String jobTitle)   { this.jobTitle   = jobTitle; }

    // ─── Utility ──────────────────────────────────────────────────────────────

    /** Displays job title info in a formatted way */
    public void displayInfo() {
        System.out.println("  Job Title ID  : " + jobTitleId);
        System.out.println("  Job Title     : " + jobTitle);
    }

    /** toString for debugging */
    @Override
    public String toString() {
        return "JobTitle{" +
                "jobTitleId=" + jobTitleId +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}
