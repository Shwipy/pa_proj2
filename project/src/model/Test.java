package model;

/**
 * Class used to represent a Test done during a Repair and its attributes
 */
public class Test {

    private Integer id;
    private Integer employeeId;
    private Integer repairId;
    private String designation;
    private String description;
    private Long testDate;

    /**
     * Constructor that assigns attributes to a Test
     * @param id Id of the test
     * @param employeeId Id of the employee that did the test
     * @param repairId id of the repair that the test is being done on
     * @param designation designation of the test
     * @param description short description of the test that was done
     * @param testDate date when the test was done on the equipment being repaired
     */
    public Test(Integer id, Integer employeeId, Integer repairId, String designation, String description, Long testDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.repairId = repairId;
        this.designation = designation;
        this.description = description;
        this.testDate = testDate;
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
    public String getDesignation() { 
    	return designation; 
    }
    public String getDescription() { 
    	return description; 
    }
    public Long getTestDate() { 
    	return testDate; 
    }

    public void setId(Integer id) { 
    	this.id = id; 
    }
}
