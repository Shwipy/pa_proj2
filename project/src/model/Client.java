package model;

/**
 * Class used to represent a Client and its attributes
 */
public class Client {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Boolean active;
    private Boolean approved;
    private String email;
    private String type;
    private String nif;
    private String phoneNumber;
    private String residence;
    private String businessSector;
    private String tier;

    /**
     * Constructor that assigns attributes to a Client
     * @param id Id of the client
     * @param name Name of the client
     * @param username Username of the client
     * @param password Password of the client
     * @param active If the client account is active and not banned
     * @param approved If the client account was approved after creation
     * @param email Email of the client
     * @param type Equals the type of account in this case client
     * @param nif NIF of the client
     * @param phoneNumber Phone Number of the client
     * @param residence Residence of the client
     * @param businessSector Business Sector of the client
     * @param tier Tier of the client
     */
    public Client(Integer id, String name, String username, String password, Boolean active, Boolean approved, String email, String type, String nif, String phoneNumber, String residence, String businessSector, String tier) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.active = active;
        this.approved = approved;
        this.email = email;
        this.type = type;
        this.nif = nif;
        this.phoneNumber = phoneNumber;
        this.residence = residence;
        this.businessSector = businessSector;
        this.tier = tier;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getApproved() {
        return approved;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getNif() {
        return nif;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getResidence() {
        return residence;
    }

    public String getBusinessSector() {
        return businessSector;
    }

    public String getTier() {
        return tier;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public void setBusinessSector(String businessSector) {
        this.businessSector = businessSector;
    }

    @Override
    public String toString() {
        return "Account:" +'\n'+
                '\t' + "Username: '" + username + '\'' + '\n'+
                '\t' + "Name: '" + name + '\'' + '\n'+
                '\t' + "Email: '" + email + '\'' + '\n'+
                '\t' + "NIF: '" + nif + '\'' + '\n'+
                '\t' + "Phone Number: '" + phoneNumber + '\'' + '\n'+
                '\t' + "Residence: '" + residence + '\'' + '\n'+
                '\t' + "Business Sector: '" + businessSector + '\'' + '\n'+
                '\t' + "Tier:'" + tier + '\'';
    }
}
