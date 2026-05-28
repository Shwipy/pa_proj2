package controller;
import model.DataBase;
import model.Repair;
import java.sql.*;
import java.util.ArrayList;

/**
 * Class to manage Repairs and their Resources
 */
public class RepairController {

	/**
	 * Allows for the creation of a new Repair, by the creation and execution of a prepared statement
	 * @param repair repair data that will be added to the db
	 * @return Confirmation that the repair was or not added to the database
	 */
	public static Boolean addRepair(Repair repair) {
	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();
	        sqlQuery.append(" INSERT INTO repair (employee_id, equipment_id, repair_number, start_date, end_date, duration, price, state, notes) ");
	        sqlQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ");

	        ps = db.getConn().prepareStatement(sqlQuery.toString(), Statement.RETURN_GENERATED_KEYS);
	        ps.clearParameters();

	        if (repair.getEmployeeId() == null) {
	            ps.setNull(1, Types.INTEGER);
	        } else {
	            ps.setInt(1, repair.getEmployeeId());
	        }

	        ps.setInt(2, repair.getEquipmentId());
	        ps.setString(3, repair.getRepairNumber());
	        ps.setLong(4, repair.getStartDate());
	        

	        if (repair.getEndDate() == null) {
	            ps.setNull(5, Types.BIGINT);
	        } else {
	            ps.setLong(5, repair.getEndDate());
	        }

	        if (repair.getDuration() == null) {
	            ps.setNull(6, Types.BIGINT);
	        } else {
	            ps.setLong(6, repair.getDuration());
	        }

	        if (repair.getPrice() == null) {
	            ps.setNull(7, Types.NUMERIC);
	        } else {
	            ps.setDouble(7, repair.getPrice());
	        }

	        ps.setString(8, repair.getState());

	        if (repair.getNotes() == null || repair.getNotes().trim().equals("")) {
	            ps.setNull(9, Types.VARCHAR);
	        } else {
	            ps.setString(9, repair.getNotes());
	        }

	        ps.executeUpdate();

	        rs = ps.getGeneratedKeys();

	        while (rs.next()) {
	            int id = rs.getInt(1);
	            repair.setId(id);
	        }
	        
	        Integer clientId = getClientIdByRepair(repair.getId());

	        NotificationController. createRepairNotification(
	                repair.getRepairNumber(),
	                repair.getId(),
	                clientId
	        );
	        
	        return true;

	    } catch (SQLException e) {
	        System.out.println("!! SQL Exception !!\n" + e);
	        e.printStackTrace();
	    } finally {
	        db.closeDb();
	    }

	    return false;
	}

	/**
	 * Allows listing(10 elements) by asc or desc order of the repairs of one employee
	 * @param i variable to indicate the page
	 * @param asc the order the items in the listes are in asc or desc
	 * @param orderBy variable to identify which variable the list is being ordered by, start date/employee number
	 * @param employeeId id of the employee with the repairs in question
	 * @return Confirmation that the repair was or not added to the database
	 */
	public static ArrayList<Repair> listRepairsByEmployee(Integer i, Boolean asc, String orderBy, int employeeId) {

	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet result = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();

	        if (orderBy == null || orderBy.equals("date")) {

	            if (asc) {
	                sqlQuery.append(" SELECT * FROM repair WHERE employee_id = ? ORDER BY start_date ASC LIMIT 10 OFFSET ? ");
	            } else {
	                sqlQuery.append(" SELECT * FROM repair WHERE employee_id = ? ORDER BY start_date DESC LIMIT 10 OFFSET ? ");
	            }

	        } else {

	            if (asc) {
	                sqlQuery.append(" SELECT * FROM repair WHERE employee_id = ? ORDER BY repair_number ASC LIMIT 10 OFFSET ? ");
	            } else {
	                sqlQuery.append(" SELECT * FROM repair WHERE employee_id = ? ORDER BY repair_number DESC LIMIT 10 OFFSET ? ");
	            }
	        }

	        ps = db.getConn().prepareStatement(sqlQuery.toString());
	        ps.clearParameters();
	        ps.setInt(1, employeeId);
	        ps.setInt(2, 10 * i);

	        result = ps.executeQuery();

	        ArrayList<Repair> repairs = new ArrayList<Repair>();

	        while (result.next()) {

	            Double price = null;

	            if (result.getBigDecimal("price") != null) {
	                price = result.getBigDecimal("price").doubleValue();
	            }

	            Repair repair = new Repair(
	                    result.getInt("id"),
	                    (Integer) result.getObject("employee_id"),
	                    result.getInt("equipment_id"),
	                    result.getString("repair_number"),
	                    result.getLong("start_date"),
	                    (Long) result.getObject("end_date"),
	                    (Long) result.getObject("duration"),
	                    price,
	                    result.getString("state"),
	                    result.getString("notes")
	            );

	            repairs.add(repair);
	        }

	        return repairs;

	    } catch (SQLException e) {
	        System.out.println("!! SQL Exception !!\n" + e);
	        e.printStackTrace();
	    } finally {
	        db.closeDb();
	    }

	    return null;
	}

	/**
	 * Allows the verification of if an equipment is or not already being repaired
	 * @param equipmentId id of the equipment thats being verified
	 * @return Confirmation if the quipment is or not being repaired
	 */
	public static Boolean equipmentHasActiveRepair(int equipmentId) {
	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();
	        sqlQuery.append(" SELECT id ");
	        sqlQuery.append(" FROM repair ");
	        sqlQuery.append(" WHERE equipment_id = ? ");
	        sqlQuery.append(" AND state IN (?, ?, ?) ");

	        ps = db.getConn().prepareStatement(sqlQuery.toString());
	        ps.clearParameters();
	        ps.setInt(1, equipmentId);
	        ps.setString(2, "submitted");
	        ps.setString(3, "accepted");
	        ps.setString(4, "in_progress");

	        rs = ps.executeQuery();

	        if (rs.next()) {
	            return true;
	        }

	    } catch (SQLException e) {
	        System.out.println("!! SQL Exception !!\n" + e);
	        e.printStackTrace();
	    } finally {
	        db.closeDb();
	    }

	    return false;
	}

	/**
	 * Allows a manager to approve of a repair
	 * @param repairId id of the repair thats being approved
	 * @return Confirmation that the repair was or approved
	 */
	public static Boolean approveRepair(int repairId) {
	    DataBase db = new DataBase();
	    PreparedStatement ps = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();
	        sqlQuery.append(" UPDATE repair ");
	        sqlQuery.append(" SET state = ? ");
	        sqlQuery.append(" WHERE id = ? AND state = ? ");

	        ps = db.getConn().prepareStatement(sqlQuery.toString());
	        ps.clearParameters();
	        ps.setString(1, "accepted");
	        ps.setInt(2, repairId);
	        ps.setString(3, "submitted");

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            return true;
	        }

	    } catch (SQLException e) {
	        System.out.println("!! SQL Exception !!\n" + e);
	        e.printStackTrace();
	    } finally {
	        db.closeDb();
	    }

	    return false;
	}

	/**
	 * Allows a manager to reject a repair
	 * @param repairId id of the repair thats being rejected
	 * @return Confirmation that the repair was or rejected
	 */
	public static Boolean rejectRepair(int repairId) {
	    DataBase db = new DataBase();
	    PreparedStatement ps = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();
	        sqlQuery.append(" UPDATE repair ");
	        sqlQuery.append(" SET state = ? ");
	        sqlQuery.append(" WHERE id = ? AND state = ? ");

	        ps = db.getConn().prepareStatement(sqlQuery.toString());
	        ps.clearParameters();
	        ps.setString(1, "archived");
	        ps.setInt(2, repairId);
	        ps.setString(3, "submitted");

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            return true;
	        }

	    } catch (SQLException e) {
	        System.out.println("!! SQL Exception !!\n" + e);
	        e.printStackTrace();
	    } finally {
	        db.closeDb();
	    }

	    return false;
	}

	/**
	 * Allows an employee to accept doing a repair
	 * @param repairId id of the repair thats being accepted
	 * @param employeeId id of the employee accepting the repair
	 * @return Confirmation that the repair was or accepted
	 */
	public static Boolean acceptRepair(Integer repairId,Integer employeeId) {
		DataBase db = new DataBase();
		PreparedStatement ps = null;

		try {
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE repair ");
			sqlQuery.append(" SET employee_id = ?,state = ? ");
			sqlQuery.append(" WHERE id = ? AND state = ? ");

			ps = db.getConn().prepareStatement(sqlQuery.toString());
			ps.clearParameters();
			ps.setInt(1, employeeId);
			ps.setString(2, "in_progress");
			ps.setInt(3, repairId);
			ps.setString(4, "accepted");

			int rows = ps.executeUpdate();

			if (rows > 0) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println("!! SQL Exception !!\n" + e);
			e.printStackTrace();
		} finally {
			db.closeDb();
		}

		return false;
	}

	/**
	 * Verification that a repair actually exists
	 * @param repairId id of the repair thats being verified
	 * @return Confirmation that the repair exists or false if it doesnt
	 */
	 public static Boolean existsSubmittedRepair(int repairId) {
	        DataBase db = new DataBase();
	        PreparedStatement ps = null;
	        ResultSet rs = null;

	        try {
	            StringBuffer sqlQuery = new StringBuffer();
	            sqlQuery.append(" SELECT id ");
	            sqlQuery.append(" FROM repair ");
	            sqlQuery.append(" WHERE id = ? AND state = ? ");

	            ps = db.getConn().prepareStatement(sqlQuery.toString());
	            ps.clearParameters();
	            ps.setInt(1, repairId);
	            ps.setString(2, "submitted");

	            rs = ps.executeQuery();

	            if (rs.next()) {
	                return true;
	            }

	        } catch (SQLException e) {
	            System.out.println("!! SQL Exception !!\n" + e);
	            e.printStackTrace();
	        } finally {
	            db.closeDb();
	        }

	        return false;
	 }

	/**
	 * Makes sure that only the employee thats doing the repair can acess it
	 * @param repairId id of the repair in question
	 * @param employeeId id of the employee being verified
	 * @return Confirmation if the employee is or not working on the repair
	 */
	 public static Boolean existsAssignedRepairForEmployee(int repairId, int employeeId) {
	        DataBase db = new DataBase();
	        PreparedStatement ps = null;
	        ResultSet rs = null;

	        try {
	            StringBuffer sqlQuery = new StringBuffer();
	            sqlQuery.append(" SELECT id ");
	            sqlQuery.append(" FROM repair ");
	            sqlQuery.append(" WHERE id = ? AND employee_id = ? ");

	            ps = db.getConn().prepareStatement(sqlQuery.toString());
	            ps.clearParameters();
	            ps.setInt(1, repairId);
	            ps.setInt(2, employeeId);

	            rs = ps.executeQuery();

	            if (rs.next()) {
	                return true;
	            }

	        } catch (SQLException e) {
	            System.out.println("!! SQL Exception !!\n" + e);
	            e.printStackTrace();
	        } finally {
	            db.closeDb();
	        }

	        return false;
	    }

	/**
	 * Allow a repair state to be set to accepted after an employee agrees to the repair
	 * @param repairId id of the repair that was accepted
	 * @return Confirmation if the procedure was or not successful
	 */
	 public static Boolean startRepair(int repairId, int employeeId) {
	        DataBase db = new DataBase();
	        PreparedStatement ps = null;

	        try {
	            StringBuffer sqlQuery = new StringBuffer();
	            sqlQuery.append(" UPDATE repair ");
	            sqlQuery.append(" SET state = ? ");
	            sqlQuery.append(" WHERE id = ? AND employee_id = ? AND state = ? ");

	            ps = db.getConn().prepareStatement(sqlQuery.toString());
	            ps.clearParameters();
	            ps.setString(1, "in_progress");
	            ps.setInt(2, repairId);
	            ps.setInt(3, employeeId);
	            ps.setString(4, "accepted");

	            int rows = ps.executeUpdate();

	            if (rows > 0) {
	                return true;
	            }

	        } catch (SQLException e) {
	            System.out.println("!! SQL Exception !!\n" + e);
	            e.printStackTrace();
	        } finally {
	            db.closeDb();
	        }

	        return false;
	    }

	/**
	 * Allows an employee to set a repair as finished
	 * @param repairId id of the repair that's being marked as finished
	 * @param employeeId id of the emlpoyee thats working on the repair
	 * @param notes field where the employee can write some notes about the repair
	 * @param price final price being attributed to the repair
	 * @return Confirmation that the repair was finished with success
	 */
	 public static Boolean finishRepair(int repairId, int employeeId, String notes, Double price) {
		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    PreparedStatement psSelect = null;
		    PreparedStatement psEquipment = null;
		    ResultSet rs = null;

		    try {
		        Long startDate = null;
		        Integer equipmentId = null;

		        StringBuffer sqlQuery = new StringBuffer();
		        sqlQuery.append(" SELECT start_date, equipment_id ");
		        sqlQuery.append(" FROM repair ");
		        sqlQuery.append(" WHERE id = ? AND employee_id = ? AND state = ? ");

		        psSelect = db.getConn().prepareStatement(sqlQuery.toString());
		        psSelect.clearParameters();
		        psSelect.setInt(1, repairId);
		        psSelect.setInt(2, employeeId);
		        psSelect.setString(3, "in_progress");

		        rs = psSelect.executeQuery();

		        if (rs.next()) {
		            startDate = rs.getLong("start_date");
		            equipmentId = rs.getInt("equipment_id");
		        } else {
		            return false;
		        }

		        long endDate = System.currentTimeMillis() / 1000;
		        long duration = endDate - startDate;

		        sqlQuery = new StringBuffer();
		        sqlQuery.append(" UPDATE repair ");
		        sqlQuery.append(" SET end_date = ?, duration = ?, price = ?, notes = ?, state = ? ");
		        sqlQuery.append(" WHERE id = ? AND employee_id = ? AND state = ? ");

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();
		        ps.setLong(1, endDate);
		        ps.setLong(2, duration);

		        if (price == null) {
		            ps.setNull(3, Types.NUMERIC);
		        } else {
		            ps.setDouble(3, price);
		        }

		        if (notes == null || notes.trim().equals("")) {
		            ps.setNull(4, Types.VARCHAR);
		        } else {
		            ps.setString(4, notes);
		        }

		        ps.setString(5, "finished");
		        ps.setInt(6, repairId);
		        ps.setInt(7, employeeId);
		        ps.setString(8, "in_progress");

		        int rows = ps.executeUpdate();

		        if (rows > 0) {
		            sqlQuery = new StringBuffer();
		            sqlQuery.append(" UPDATE equipment ");
		            sqlQuery.append(" SET repair_date = ? ");
		            sqlQuery.append(" WHERE id = ? ");

		            psEquipment = db.getConn().prepareStatement(sqlQuery.toString());
		            psEquipment.clearParameters();
		            psEquipment.setLong(1, endDate);
		            psEquipment.setInt(2, equipmentId);

		            psEquipment.executeUpdate();
		            
		            Integer clientId = getClientIdByRepair(repairId);

		            if (clientId != null) {
		                NotificationController.finishRepairNotification(clientId, repairId);
		            }
		            
		            return true;
		        }

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return false;
		}

	/**
	 * Allows for the listing of all available repairs in the database in groups of 10
	 * @param i variable to indicate the page
	 * @param asc the order the items in the listed are in asc or desc
	 * @param orderBy variable to identify which variable the list is being ordered by, start date/employee number
	 * @return An array list containing 10 elements from the list
	 */
	 public static ArrayList<Repair> listAllRepairs() {

		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet result = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();

		        sqlQuery.append(" SELECT * FROM repair ");

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();

		        result = ps.executeQuery();

		        ArrayList<Repair> repairs = new ArrayList<Repair>();

		        while (result.next()) {

		            Double price = null;

		            if (result.getBigDecimal("price") != null) {
		                price = result.getBigDecimal("price").doubleValue();
		            }

		            Repair repair = new Repair(
		                    result.getInt("id"),
		                    (Integer) result.getObject("employee_id"),
		                    result.getInt("equipment_id"),
		                    result.getString("repair_number"),
		                    result.getLong("start_date"),
		                    (Long) result.getObject("end_date"),
		                    (Long) result.getObject("duration"),
		                    price,
		                    result.getString("state"),
		                    result.getString("notes")
		            );

		            repairs.add(repair);
		        }

		        return repairs;

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}

	/**
	 * Allows for the generation of the repair number
	 * @return The generated repair number
	 */
	 public static String generateRepairNumber() {

		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet rs = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();
		        sqlQuery.append(" SELECT COUNT(*) AS total ");
		        sqlQuery.append(" FROM repair ");

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();

		        rs = ps.executeQuery();

		        if (rs.next()) {
		            int nextNumber = rs.getInt("total") + 1;
		            //https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html#now--
		            java.time.LocalDateTime now = java.time.LocalDateTime.now();
		            //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ofPattern-java.lang.String-
		            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

		            String datePart = now.format(formatter);

		            return nextNumber + datePart;
		        }

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}
	/**
	 * Allow to get the state of a repair from the database based on its id
	 * @param repairId id of the repair we want he state of
	 * @return A string with the current state of the repair
	 */
	 public static String getRepairState(int repairId) {
		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet rs = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();
		        sqlQuery.append(" SELECT state ");
		        sqlQuery.append(" FROM repair ");
		        sqlQuery.append(" WHERE id = ? ");

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();
		        ps.setInt(1, repairId);

		        rs = ps.executeQuery();

		        if (rs.next()) {
		            return rs.getString("state");
		        }

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}

	/**
	 * Allow to get the data from a repair from the database based on its id
	 * @param repairId id of the repair we want the data of
	 * @return An object repair with all the data from the repair we want
	 */
	 public static Repair getRepair(int repairId) {

		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet result = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();
		        sqlQuery.append(" SELECT * FROM repair WHERE id = ? ");

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();
		        ps.setInt(1, repairId);

		        result = ps.executeQuery();

		        if (result.next()) {

		            Double price = null;

		            if (result.getBigDecimal("price") != null) {
		                price = result.getBigDecimal("price").doubleValue();
		            }

		            Repair repair = new Repair(
		                    result.getInt("id"),
		                    (Integer) result.getObject("employee_id"),
		                    result.getInt("equipment_id"),
		                    result.getString("repair_number"),
		                    result.getLong("start_date"),
		                    (Long) result.getObject("end_date"),
		                    (Long) result.getObject("duration"),
		                    price,
		                    result.getString("state"),
		                    result.getString("notes")
		            );

		            return repair;
		        }

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}

	/**
	 * Allows for the listing of all unfinished repairs in the database in groups of 10
	 * @param i variable to indicate the page
	 * @param asc the order the items in the listed are in asc or desc
	 * @return An array list containing 10 elements from the list
	 */
	 public static ArrayList<Repair> listUnfinishedRepairs(Integer i, Boolean asc) {

		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet result = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();

		        if (asc) {
		            sqlQuery.append(" SELECT * FROM repair WHERE state IN ('submitted','accepted','in_progress') ORDER BY repair_number ASC LIMIT 10 OFFSET ? ");
		        } else {
		            sqlQuery.append(" SELECT * FROM repair WHERE state IN ('submitted','accepted','in_progress') ORDER BY repair_number DESC LIMIT 10 OFFSET ? ");
		        }

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();
		        ps.setInt(1, 10 * i);

		        result = ps.executeQuery();

		        ArrayList<Repair> repairs = new ArrayList<Repair>();

		        while (result.next()) {

		            Double price = null;

		            if (result.getBigDecimal("price") != null) {
		                price = result.getBigDecimal("price").doubleValue();
		            }

		            Repair repair = new Repair(
		                    result.getInt("id"),
		                    (Integer) result.getObject("employee_id"),
		                    result.getInt("equipment_id"),
		                    result.getString("repair_number"),
		                    result.getLong("start_date"),
		                    (Long) result.getObject("end_date"),
		                    (Long) result.getObject("duration"),
		                    price,
		                    result.getString("state"),
		                    result.getString("notes")
		            );

		            repairs.add(repair);
		        }

		        return repairs;

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}

	/**
	 * Allows for the listing of all available repairs in the database in groups of 10 for a certain user
	 * @param i variable to indicate the page
	 * @param asc the order the items in the listed are in asc or desc
	 * @param orderBy variable to identify which variable the list is being ordered by, start date/repair number
	 * @param clientId client that created the repairs
	 * @return An array list containing 10 repairs a client
	 */
	 public static ArrayList<Repair> listRepairsByClient(Integer i, Boolean asc, String orderBy, int clientId) {

		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet result = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();

		        if (orderBy == null || orderBy.equals("date")) {

		            if (asc) {
		                sqlQuery.append(" SELECT repair.* FROM repair, equipment WHERE repair.equipment_id = equipment.id AND equipment.client_id = ? ORDER BY repair.start_date ASC LIMIT 10 OFFSET ? ");
		            } else {
		                sqlQuery.append(" SELECT repair.* FROM repair, equipment WHERE repair.equipment_id = equipment.id AND equipment.client_id = ? ORDER BY repair.start_date DESC LIMIT 10 OFFSET ? ");
		            }

		        } else {

		            if (asc) {
		                sqlQuery.append(" SELECT repair.* FROM repair, equipment WHERE repair.equipment_id = equipment.id AND equipment.client_id = ? ORDER BY repair.repair_number ASC LIMIT 10 OFFSET ? ");
		            } else {
		                sqlQuery.append(" SELECT repair.* FROM repair, equipment WHERE repair.equipment_id = equipment.id AND equipment.client_id = ? ORDER BY repair.repair_number DESC LIMIT 10 OFFSET ? ");
		            }
		        }

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();
		        ps.setInt(1, clientId);
		        ps.setInt(2, 10 * i);

		        result = ps.executeQuery();

		        ArrayList<Repair> repairs = new ArrayList<Repair>();

		        while (result.next()) {

		            Double price = null;

		            if (result.getBigDecimal("price") != null) {
		                price = result.getBigDecimal("price").doubleValue();
		            }

		            Repair repair = new Repair(
		                    result.getInt("id"),
		                    (Integer) result.getObject("employee_id"),
		                    result.getInt("equipment_id"),
		                    result.getString("repair_number"),
		                    result.getLong("start_date"),
		                    (Long) result.getObject("end_date"),
		                    (Long) result.getObject("duration"),
		                    price,
		                    result.getString("state"),
		                    result.getString("notes")
		            );

		            repairs.add(repair);
		        }

		        return repairs;

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}

	/**
	 * Allow searching for a string in the repair table
	 *
	 * @param search   string being searched for in the database
	 * @param type     type of field we are searching on, number/state
	 * @param selected
	 * @return An array list containing 10 elements from the filtered results
	 */
	//https://www.postgresql.org/docs/current/functions-matching.html
	 public static ArrayList<Repair> searchRepairs(String search, String type, Boolean selected) {
		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet result = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();
		        if (selected){
					if (type.equals("repair number")) {
						sqlQuery.append(" SELECT * FROM repair WHERE repair_number ILIKE ? AND state != 'finished' ORDER BY id ASC ");
					} else if (type.equals("repair state")) {
						sqlQuery.append(" SELECT * FROM repair WHERE state ILIKE ? AND state != 'finished' ORDER BY id ASC ");
					} else if (type.equals("client name")) {
						sqlQuery.append(" SELECT * FROM repair INNER JOIN account ON repair.employee_id = account.id WHERE name ILIKE ? AND state != 'finished' ORDER BY repair.id ASC ");
					}

				}else{
					if (type.equals("repair number")) {
						sqlQuery.append(" SELECT * FROM repair WHERE repair_number ILIKE ? ORDER BY id ASC ");
					} else if (type.equals("repair state")) {
						sqlQuery.append(" SELECT * FROM repair WHERE state ILIKE ? ORDER BY id ASC ");
					} else if (type.equals("client name")) {
						sqlQuery.append(" SELECT * FROM repair INNER JOIN account ON repair.employee_id = account.id WHERE name ILIKE ? ORDER BY repair.id ASC ");
					}
				}

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();
		        ps.setString(1, "%" + search + "%");

		        result = ps.executeQuery();

		        ArrayList<Repair> filteredRepairs = new ArrayList<Repair>();

		        while (result.next()) {

		            Double price = null;

		            if (result.getBigDecimal("price") != null) {
		                price = result.getBigDecimal("price").doubleValue();
		            }

		            Repair repair = new Repair(
		                    result.getInt("id"),
		                    (Integer) result.getObject("employee_id"),
		                    result.getInt("equipment_id"),
		                    result.getString("repair_number"),
		                    result.getLong("start_date"),
		                    (Long) result.getObject("end_date"),
		                    (Long) result.getObject("duration"),
		                    price,
		                    result.getString("state"),
		                    result.getString("notes")
		            );

		            filteredRepairs.add(repair);
		        }

		        return filteredRepairs;

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}

	/**
	 * Allow a client to search for a string in the list of his repairs
	 * @param search string being searched for in the database
	 * @param type type of field we are searching on, repair number/state
	 * @return An array list containing 10 elements from the filtered results
	 */
	 //https://www.postgresql.org/docs/current/functions-matching.html
	 public static ArrayList<Repair> searchRepairsByClient(String search, String type, int clientId) {
		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet result = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();

		        if (type.equals("number")) {
		            sqlQuery.append(" SELECT repair.* FROM repair, equipment WHERE repair.equipment_id = equipment.id AND equipment.client_id = ? AND repair.repair_number ILIKE ? ORDER BY repair.id ASC ");
		        } else {
		            sqlQuery.append(" SELECT repair.* FROM repair, equipment WHERE repair.equipment_id = equipment.id AND equipment.client_id = ? AND repair.state ILIKE ? ORDER BY repair.id ASC ");
		        }

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();
		        ps.setInt(1, clientId);
		        ps.setString(2, "%" + search + "%");

		        result = ps.executeQuery();

		        ArrayList<Repair> filteredRepairs = new ArrayList<Repair>();

		        while (result.next()) {

		            Double price = null;

		            if (result.getBigDecimal("price") != null) {
		                price = result.getBigDecimal("price").doubleValue();
		            }

		            Repair repair = new Repair(
		                    result.getInt("id"),
		                    (Integer) result.getObject("employee_id"),
		                    result.getInt("equipment_id"),
		                    result.getString("repair_number"),
		                    result.getLong("start_date"),
		                    (Long) result.getObject("end_date"),
		                    (Long) result.getObject("duration"),
		                    price,
		                    result.getString("state"),
		                    result.getString("notes")
		            );

		            filteredRepairs.add(repair);
		        }

		        return filteredRepairs;

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}

	/**
	 * Allow an employee to search for a string in the list of repairs he's working on
	 * @param search string being searched for in the database
	 * @param type type of field we are searching on, repair number/state
	 * @return An array list containing 10 elements from the filtered results
	 */
	//https://www.postgresql.org/docs/current/functions-matching.html
	 public static ArrayList<Repair> searchRepairsByEmployee(String search, String type, int employeeId) {
		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet result = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();

		        if (type.equals("number")) {
		            sqlQuery.append(" SELECT * FROM repair WHERE employee_id = ? AND repair_number ILIKE ? ORDER BY id ASC ");
		        } else {
		            sqlQuery.append(" SELECT * FROM repair WHERE employee_id = ? AND state ILIKE ? ORDER BY id ASC ");
		        }

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();
		        ps.setInt(1, employeeId);
		        ps.setString(2, "%" + search + "%");

		        result = ps.executeQuery();

		        ArrayList<Repair> filteredRepairs = new ArrayList<Repair>();

		        while (result.next()) {

		            Double price = null;

		            if (result.getBigDecimal("price") != null) {
		                price = result.getBigDecimal("price").doubleValue();
		            }

		            Repair repair = new Repair(
		                    result.getInt("id"),
		                    (Integer) result.getObject("employee_id"),
		                    result.getInt("equipment_id"),
		                    result.getString("repair_number"),
		                    result.getLong("start_date"),
		                    (Long) result.getObject("end_date"),
		                    (Long) result.getObject("duration"),
		                    price,
		                    result.getString("state"),
		                    result.getString("notes")
		            );

		            filteredRepairs.add(repair);
		        }

		        return filteredRepairs;

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}

	/**
	 * Function to get the id of the client that submitted a certain repair
	 * @param repairId id of the repair we want the original user id from
	 * @return The id of the creator of the repair
	 */
	 public static Integer getClientIdByRepair(int repairId) {
		    DataBase db = new DataBase();
		    PreparedStatement ps = null;
		    ResultSet rs = null;

		    try {
		        StringBuffer sqlQuery = new StringBuffer();
		        sqlQuery.append(" SELECT equipment.client_id FROM repair, equipment WHERE repair.equipment_id = equipment.id AND repair.id = ? ");

		        ps = db.getConn().prepareStatement(sqlQuery.toString());
		        ps.clearParameters();
		        ps.setInt(1, repairId);

		        rs = ps.executeQuery();

		        if (rs.next()) {
		            return rs.getInt("client_id");
		        }

		    } catch (SQLException e) {
		        System.out.println("!! SQL Exception !!\n" + e);
		        e.printStackTrace();
		    } finally {
		        db.closeDb();
		    }

		    return null;
		}
}
