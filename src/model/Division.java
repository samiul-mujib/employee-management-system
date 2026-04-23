package model;

/**
 * Division.java
 * CSc3350 Team Project SP2026 — Team DataForge
 * Author: Manna
 *
 * Model class representing a Division record from the division table.
 * Fields match the database columns exactly.
 *
 * DB Table: division
 * Columns: ID, Name, city, addressLine1, addressLine2, state, country, postalCode
 */
public class Division {

    // Fields matching DB columns
    private int    id;
    private String name;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String country;
    private String postalCode;

    // ─── Constructors ─────────────────────────────────────────────────────────

    /** Default constructor */
    public Division() {}

    /** Full constructor — used when loading from DB */
    public Division(int id, String name, String city, String addressLine1,
                    String addressLine2, String state, String country, String postalCode) {
        this.id           = id;
        this.name         = name;
        this.city         = city;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.state        = state;
        this.country      = country;
        this.postalCode   = postalCode;
    }

    /** Constructor without ID — used when adding new division */
    public Division(String name, String city, String addressLine1,
                    String addressLine2, String state, String country, String postalCode) {
        this.name         = name;
        this.city         = city;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.state        = state;
        this.country      = country;
        this.postalCode   = postalCode;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public int    getId()           { return id; }
    public String getName()         { return name; }
    public String getCity()         { return city; }
    public String getAddressLine1() { return addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public String getState()        { return state; }
    public String getCountry()      { return country; }
    public String getPostalCode()   { return postalCode; }

    // ─── Setters ──────────────────────────────────────────────────────────────

    public void setId(int id)                     { this.id           = id; }
    public void setName(String name)               { this.name         = name; }
    public void setCity(String city)               { this.city         = city; }
    public void setAddressLine1(String addressLine1){ this.addressLine1 = addressLine1; }
    public void setAddressLine2(String addressLine2){ this.addressLine2 = addressLine2; }
    public void setState(String state)             { this.state        = state; }
    public void setCountry(String country)         { this.country      = country; }
    public void setPostalCode(String postalCode)   { this.postalCode   = postalCode; }

    // ─── Utility ──────────────────────────────────────────────────────────────

    /** Displays division info in a formatted way */
    public void displayInfo() {
        System.out.println("========================================");
        System.out.println("  Division ID   : " + id);
        System.out.println("  Name          : " + name);
        System.out.println("  City          : " + city);
        System.out.println("  Address 1     : " + addressLine1);
        System.out.println("  Address 2     : " + (addressLine2 != null ? addressLine2 : "N/A"));
        System.out.println("  State         : " + state);
        System.out.println("  Country       : " + country);
        System.out.println("  Postal Code   : " + postalCode);
        System.out.println("========================================");
    }

    /** toString for debugging */
    @Override
    public String toString() {
        return "Division{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
