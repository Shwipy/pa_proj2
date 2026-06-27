package model;

import controller.OutputController;

/**
 * Class used to represent a Repair done to an Equipment and its attributes
 */
public class Repair {
    private Integer id;
    private Integer employeeId;
    private Integer equipmentId;
    private String repairNumber;
    private Long startDate;
    private Long endDate;
    private Long duration;
    private Double price;
    private String state;
    private String notes;

    /**
     * Constructor that assigns attributes to a Repair
     * @param id Id of the repair
     * @param employeeId Id of the employee assigned to the repair, null while not being repaired
     * @param equipmentId id of the requested equipment for repair
     * @param repairNumber string of numbers to identify the repair
     * @param startDate date when the repair request was created
     * @param endDate date when the repair request was concluded
     * @param duration time duration of the repair in seconds
     * @param price final price of the requested repair
     * @param state current state of the repair
     * @param notes notes added by the employee at the end of the repair
     */
    public Repair(Integer id, Integer employeeId, Integer equipmentId, String repairNumber, Long startDate, Long endDate, Long duration, Double price, String state, String notes) {
        this.id = id;
        this.employeeId = employeeId;
        this.equipmentId = equipmentId;
        this.repairNumber = repairNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.price = price;
        this.state = state;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public String getRepairNumber() {
        return repairNumber;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public Long getDuration() {
        return duration;
    }

    public Double getPrice() {
        return price;
    }

    public String getState() {
        return state;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Object[] toTableRow() {
        return new Object[]{
                this.getId(),
                this.getRepairNumber(),
                this.getEmployeeId(),
                this.getEquipmentId(),
                OutputController.dateFormater(this.getStartDate(),"date"),
                this.getEndDate() == null ? "Not repaired yet" : OutputController.dateFormater(this.getEndDate(),"day"),
                this.getDuration(),
                this.getPrice(),
                this.getState(),
                this.getNotes()
        };
    }
    
    public String toFormatedString() {
        return "[id=" + id +
                ", employeeId=" + employeeId +
                ", equipmentId=" + equipmentId +
                ", repairNumber=" + repairNumber +
                ", startDate=" + OutputController.dateFormater(startDate,"date") +
                ", endDate=" + OutputController.dateFormater(endDate,"date") +
                ", duration=" + duration +
                ", price=" + price +
                ", state=" + state +
                ", notes=" + notes + "]";
    }
    
}
