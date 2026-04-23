package model;

/**
 * Address.java
 * CSc3350 Team Project SP2026 — Team DataForge
 *
 * Model class representing an Address record from the addresses table.
 * Fields match the database columns exactly.
 *
 * DB Table: addresses
 * Columns: addressID, street, cityID, stateID, zip, DOB, phone,
 *          emergency_contact_name, emergency_contact_phone
 */
public class Address {

    // Fields matching DB columns
    private int    addressID;
    private String street;
    private int    cityID;
    private int    stateID;
    private String zip;
    private String dob;                    // Date of Birth — YYYY-MM-DD
    private String phone;
    private String emergencyContactName;
    private String emergencyContactPhone;

    // ─── Constructors ────────────────────────────────────────────────────────

    /** Default constructor */
    public Address() {}

    /** Full constructor — used when loading from DB */
    public Address(int addressID, String street, int cityID, int stateID,
                   String zip, String dob, String phone,
                   String emergencyContactName, String emergencyContactPhone) {
        this.addressID             = addressID;
        this.street                = street;
        this.cityID                = cityID;
        this.stateID               = stateID;
        this.zip                   = zip;
        this.dob                   = dob;
        this.phone                 = phone;
        this.emergencyContactName  = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
    }

    /** Constructor without addressID — used when adding new address (AUTO_INCREMENT) */
    public Address(String street, int cityID, int stateID, String zip,
                   String dob, String phone,
                   String emergencyContactName, String emergencyContactPhone) {
        this.street                = street;
        this.cityID                = cityID;
        this.stateID               = stateID;
        this.zip                   = zip;
        this.dob                   = dob;
        this.phone                 = phone;
        this.emergencyContactName  = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────

    public int    getAddressID()             { return addressID; }
    public String getStreet()                { return street; }
    public int    getCityID()                { return cityID; }
    public int    getStateID()               { return stateID; }
    public String getZip()                   { return zip; }
    public String getDob()                   { return dob; }
    public String getPhone()                 { return phone; }
    public String getEmergencyContactName()  { return emergencyContactName; }
    public String getEmergencyContactPhone() { return emergencyContactPhone; }

    // ─── Setters ─────────────────────────────────────────────────────────────

    public void setAddressID(int addressID)                         { this.addressID             = addressID; }
    public void setStreet(String street)                             { this.street                = street; }
    public void setCityID(int cityID)                               { this.cityID                = cityID; }
    public void setStateID(int stateID)                             { this.stateID               = stateID; }
    public void setZip(String zip)                                   { this.zip                   = zip; }
    public void setDob(String dob)                                   { this.dob                   = dob; }
    public void setPhone(String phone)                               { this.phone                 = phone; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName  = emergencyContactName; }
    public void setEmergencyContactPhone(String emergencyContactPhone){ this.emergencyContactPhone = emergencyContactPhone; }

    // ─── Utility ─────────────────────────────────────────────────────────────

    /** Displays address info in a formatted way */
    public void displayInfo() {
        System.out.println("  Street        : " + street);
        System.out.println("  City ID       : " + cityID);
        System.out.println("  State ID      : " + stateID);
        System.out.println("  ZIP           : " + zip);
        System.out.println("  Date of Birth : " + dob);
        System.out.println("  Phone         : " + phone);
        System.out.println("  Emergency     : " + emergencyContactName + " (" + emergencyContactPhone + ")");
    }

    /** toString for debugging */
    @Override
    public String toString() {
        return "Address{" +
                "addressID=" + addressID +
                ", street='" + street + '\'' +
                ", zip='" + zip + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
