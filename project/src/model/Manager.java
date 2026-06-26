package model;

/**
 * Class used to represent a Manager and its attributes
 * ´Sometimes used to represent an Account
 */
public class Manager {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Boolean active;
    private Boolean approved;
    private String email;
    private  String pic;
    private String type;

    /**
     * Constructor that assigns attributes to a Manager
     * @param id Id of the Manager
     * @param name Name of the Manager
     * @param username Username of the Manager
     * @param password Password of the Manager
     * @param active If the Manager account is active and not banned
     * @param approved If the Manager account was approved after creation
     * @param email Email of the Manager
     * @param pic Picture of the Manager
     * @param type Equals the type of account in this case Manager
     */
    public Manager(Integer id, String name, String username, Boolean active, Boolean approved, String password, String email, String pic, String type) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.active = active;
        this.approved = approved;
        this.password = password;
        this.email = email;
        this.pic = pic;
        this.type = type;
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

    public String getPic() {
        return pic;
    }

    public String getType() {
        return type;
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
    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "[id=" + id +
                ",\tname=" + name +
                ",\tusername=" + username +
                ",\tpassword=" + password +
                ",\tactive=" + active +
                ",\tapproved=" + approved +
                ",\temail=" + email +
                ",\ttype=" + type+"]";
    }

    /**
     * Does the same as toString, but its vertically formated showing all the attributes of an account in a formated way
     */
    public String toFormatedString() {
        return "Account:" +'\n'+
                '\t' + "Username: '" + username + '\'' + '\n'+
                '\t' + "Name: '" + name + '\'' + '\n'+
                '\t' + "Email: '" + email + '\'';
    }

    public Object[] toTableRow(){
        return new Object[]{
                this.getId(),
                this.getName(),
                this.getUsername(),
                this.getActive(),
                this.getApproved(),
                this.getEmail(),
                this.getType()
        };
    }
}
