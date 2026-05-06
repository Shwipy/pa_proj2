package model;

import controller.OutputController;

/**
 * Class used to represent an Employee and its attributes
 */
public class Employee {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Boolean active;
    private Boolean approved;
    private  String email;
    private String type;
    private String nif;
    private String phoneNumber;
    private String residence;
    //mudar na bd para varchar
    private String lvlExpertise;
    private Long startDate;

    /**
     * Constructor that assigns attributes to an Employee
     * @param id Id of the employee
     * @param name Name of the employee
     * @param username Username of the employee
     * @param password Password of the employee
     * @param active If the employee account is active and not banned
     * @param approved If the employee account was approved after creation
     * @param email Email of the employee
     * @param type Equals the type of account in this case employee
     * @param nif NIF of the employee
     * @param phoneNumber Phone Number of the employee
     * @param residence Residence of the employee
     * @param lvlExpertice Level of Expertise of the employee
     * @param startDate Date the employee was registered
     */
    public Employee(Integer id, String name, String username, String password, Boolean active, Boolean approved, String email, String type, String nif, String phoneNumber, String residence, String lvlExpertice, Long startDate) {
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
        this.lvlExpertise = lvlExpertice;
        this.startDate = startDate;
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

    public String getLvlExpertise() {
        return lvlExpertise;
    }

    public Long getStartDate() {
        return startDate;
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

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setLvlExpertise(String lvlExpertise) {
        this.lvlExpertise = lvlExpertise;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", approved=" + approved +
                ", type='" + type + '\'' +
                '}';
    }

    /**
     * Does the same as toString, but its vertically formated showing all the attributes of an employee in a formated way
     */
    public String toFormatedString() {
        return "Account:" +'\n'+
                '\t' + "Username: '" + username + '\'' + '\n'+
                '\t' + "Name: '" + name + '\'' + '\n'+
                '\t' + "Email: '" + email + '\'' + '\n'+
                '\t' + "NIF: '" + nif + '\'' + '\n'+
                '\t' + "Phone Number: '" + phoneNumber + '\'' + '\n'+
                '\t' + "Residence: '" + residence + '\'' + '\n'+
                '\t' + "Level of Expertise: '" + lvlExpertise + '\'' + '\n'+
                '\t' + "Start Date:'" + OutputController.dateFormater(startDate,"date") + '\'';
    }
}
