package model;

/**
 * Class used to represent a Part and its attributes
 */
public class Part {

    private Integer id;
    private Integer employeeId;
    private Integer repairId;
    private String code;
    private String designation;
    private String manufacturer;
    private Integer amount;

    /**
     * Constructor that assigns attributes to a Part
     * @param id Id of the part
     * @param employeeId this field was left during an earlier version of the app, its not used anymore
     * @param code identification code of the part
     * @param designation designation of the part
     * @param manufacturer manufacturer of the part
     * @param amount amount of this part that is available
      */
    public Part(Integer id, Integer employeeId, String code, String designation, String manufacturer, Integer amount) {
        this.id = id;
        this.employeeId = employeeId;
        this.code = code;
        this.designation = designation;
        this.manufacturer = manufacturer;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public Integer getRepairId() {
        return repairId;
    }

    public String getCode() {
        return code;
    }

    public String getDesignation() {
        return designation;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public void setRepairId(Integer repairId) {
        this.repairId = repairId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        return "Part ID: " + id +
                " | Code: " + code +
                " | Designation: " + designation +
                " | Manufacturer: " + manufacturer +
                " | Amount: " + amount;
    }
}
