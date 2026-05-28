package model;
import controller.OutputController;

/**
 * Class used to represent an Equipment and its attributes
 */
public class Equipment {
    private Integer id;
    private Integer clientId;
    private String brand;
    private String model;
    private String sku;
    private String batch;
    private Long manufactureDate;
    private Long submissionDate;
    private Long repairDate;

    /**
     * Constructor that assigns attributes to an Equipment
     * @param id Id of the equipment
     * @param clientId id of the client owener of the equipment
     * @param brand brand of the equipment
     * @param model Password of the equipment
     * @param sku If the employee account is active and not banned
     * @param batch If the employee account was approved after creation
     * @param manufactureDate manufacture date of the equipment
     * @param submissionDate date when the equipment was submitted for repair
     * @param repairDate Date when the repair on the equipment was finished
     */
    public Equipment(Integer id, Integer clientId, String brand, String model, String sku, String batch, Long manufactureDate, Long submissionDate, Long repairDate) {
        this.id = id;
        this.clientId = clientId;
        this.brand = brand;
        this.model = model;
        this.sku = sku;
        this.batch = batch;
        this.manufactureDate = manufactureDate;
        this.submissionDate = submissionDate;
        this.repairDate = repairDate;
    }

    public Integer getId() {
        return id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getSku() {
        return sku;
    }

    public String getBatch() {
        return batch;
    }

    public Long getManufactureDate() {
        return manufactureDate;
    }

    public Long getSubmissionDate() {
        return submissionDate;
    }

    public Long getRepairDate() {
        return repairDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Object[] toTableRow() {
        return new Object[]{
                this.getId(),
                this.getClientId(),
                this.getBrand(),
                this.getModel(),
                this.getSku(),
                this.getBatch(),
                this.getManufactureDate(),
                this.getSubmissionDate(),
                this.getRepairDate()
        };
    }
    
    @Override
    public String toString() {
        return "Equipment ID: " + this.id +
                "\nClient ID: " + this.clientId +
                "\nBrand: " + this.brand +
                "\nModel: " + this.model +
                "\nSKU: " + this.sku +
                "\nBatch: " + this.batch +
                "\nManufacture Date: " + OutputController.dateFormater(this.manufactureDate,"date") +
                "\nSubmission Date: " + OutputController.dateFormater(this.submissionDate,"date") +
                "\nRepair Date: " + (this.repairDate == null ? "Not repaired yet" : OutputController.dateFormater(this.repairDate,"date")) +
                "\n--------";
    }
}
