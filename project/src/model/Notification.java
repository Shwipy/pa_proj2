package model;
import controller.OutputController;

/**
 * Class used to represent a Notification and its attributes
 */
public class Notification {
    private Integer id;
    private Integer accountId;
    private String message;
    private Boolean isRead;
    private Long creationDate;
    private Integer senderID;
    private String type;
    private Integer repairId;

    /**
     * Constructor that assigns attributes to a Notification
     * @param id Id of the notification
     * @param accountId Id of the destination account of the notification, null if it was a manager
     * @param message Message contained in the notification
     * @param isRead if the notification was or not opend
     * @param creationDate date when the notification was created
     * @param senderID id of the account that send the notification, null if it was a manager
     * @param type type of notification, so the program can know what to do with each one
     * @param repairId id of the repair in question in the notification, if the notification is not about a repair is null
     */
    public Notification(Integer id, Integer accountId, String message, Boolean isRead, Long creationDate, Integer senderID, String type, Integer repairId) {
        this.id = id;
        this.accountId = accountId;
        this.message = message;
        this.isRead = isRead;
        this.creationDate = creationDate;
        this.senderID = senderID;
        this.type = type;
        this.repairId = repairId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public Integer getSenderId() {
        return senderID;
    }

    public String getType(){
        return type;
    }

    public  Integer getRepairId(){ 
    	return repairId;
    	
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }
    
    public Object[] toTableRow() {

        String readStatusIcon;
        if (this.getIsRead()){
            readStatusIcon = "✔";
        }else {
            readStatusIcon = "❌";
        };

        return new Object[]{
                this.getId(),
                this.getMessage(),
                readStatusIcon,
                //this.getIsRead(),
                OutputController.dateFormater(this.getCreationDate(), "date"),
                this.getSenderId(),
                this.getType(),
                this.getRepairId()
        };
    }

    @Override
    public String toString() {
        return "[" +
                "ID= " + id +
                ", Message= '" + message + '\'' +
                ", Sent on= " + OutputController.dateFormater(creationDate,"date") +
                ']';
    }
}
