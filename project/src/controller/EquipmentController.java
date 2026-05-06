package controller;

import model.DataBase;
import model.Equipment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Class to manage Equipment resources
 */
public class EquipmentController {

	/**
	 * Allows a client to add an equipment
	 * @param equipment equipment data that will be added to the db
	 * @return Confirmation that the equipment was or not added to the database
	 */
	public static Boolean addEquipment(Equipment equipment){

	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();
	        sqlQuery.append(" INSERT INTO equipment (client_id, brand, model, sku, batch, manufacture_date, submission_date, repair_date) ");
	        sqlQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");

	        ps = db.getConn().prepareStatement(sqlQuery.toString(), Statement.RETURN_GENERATED_KEYS);
	        ps.clearParameters();
	        ps.setInt(1, equipment.getClientId());
	        ps.setString(2, equipment.getBrand());
	        ps.setString(3, equipment.getModel());
	        ps.setString(4, equipment.getSku());
	        ps.setString(5, equipment.getBatch());
	        ps.setLong(6, equipment.getManufactureDate());
	        ps.setLong(7, equipment.getSubmissionDate());

	        if (equipment.getRepairDate() == null) {
	            ps.setNull(8, java.sql.Types.BIGINT);
	        } else {
	            ps.setLong(8, equipment.getRepairDate());
	        }

	        ps.executeUpdate();

	        rs = ps.getGeneratedKeys();

	        while (rs.next()) {
	            int id = rs.getInt(1);
	            equipment.setId(id);
	        }

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
	 * Verifies if an equipment id belongs to a certain client
	 * @param equipmentId id of the equipment to verify
	 * @param clientId client in question
	 * @return Confirmation if the equipment belongs or not to the client
	 */
	public static Boolean existsEquipmentForClient(int equipmentId, int clientId) {
	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();
	        sqlQuery.append(" SELECT id ");
	        sqlQuery.append(" FROM equipment ");
	        sqlQuery.append(" WHERE id = ? AND client_id = ? ");

	        ps = db.getConn().prepareStatement(sqlQuery.toString());
	        ps.clearParameters();
	        ps.setInt(1, equipmentId);
	        ps.setInt(2, clientId);

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
	 * Verifies if an equipment id belongs to a certain client
	 * @return Generated SKU code
	 */
	public static String generateSKU() {

	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        while (true) {
	            // https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#random--
	            int sku = 1 + (int)(Math.random() * 1000000);

	            StringBuffer sqlQuery = new StringBuffer();
	            sqlQuery.append(" SELECT id ");
	            sqlQuery.append(" FROM equipment ");
	            sqlQuery.append(" WHERE sku = ? ");

	            ps = db.getConn().prepareStatement(sqlQuery.toString());
	            ps.clearParameters();
	            ps.setString(1, String.valueOf(sku));

	            rs = ps.executeQuery();

	            if (!rs.next()) {
	                return String.valueOf(sku);
	            }

	            rs = null;
	            ps = null;
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
	 * Query to get 10 elements from a certain clients equipments
	 * @param i parameter used to indicate the page
	 * @param asc indicates que order the data appears in asc or desc
	 * @param orderBy indicates which parameter to order the results by
	 * @param clientId owner of the equipments that will be listed
	 * @return an ArrayList with 10 entries from a clients equipment list
	 */
	public static ArrayList<Equipment> listEquipmentsByClient(Integer i, Boolean asc, String orderBy, int clientId) {

	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet result = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();

	        if (orderBy == null || orderBy.equals("brand")) {

	            if (asc) {
	                sqlQuery.append(" SELECT * FROM equipment WHERE client_id = ? ORDER BY brand ASC LIMIT 10 OFFSET ? ");
	            } else {
	                sqlQuery.append(" SELECT * FROM equipment WHERE client_id = ? ORDER BY brand DESC LIMIT 10 OFFSET ? ");
	            }

	        } else {

	            if (asc) {
	                sqlQuery.append(" SELECT * FROM equipment WHERE client_id = ? ORDER BY sku ASC LIMIT 10 OFFSET ? ");
	            } else {
	                sqlQuery.append(" SELECT * FROM equipment WHERE client_id = ? ORDER BY sku DESC LIMIT 10 OFFSET ? ");
	            }
	        }

	        ps = db.getConn().prepareStatement(sqlQuery.toString());
	        ps.clearParameters();
	        ps.setInt(1, clientId);
	        ps.setInt(2, 10 * i);

	        result = ps.executeQuery();

	        ArrayList<Equipment> equipments = new ArrayList<Equipment>();

	        while (result.next()) {

	            Equipment equipment = new Equipment(
	                    result.getInt("id"),
	                    result.getInt("client_id"),
	                    result.getString("brand"),
	                    result.getString("model"),
	                    result.getString("sku"),
	                    result.getString("batch"),
	                    result.getLong("manufacture_date"),
	                    result.getLong("submission_date"),
	                    (Long) result.getObject("repair_date")
	            );

	            equipments.add(equipment);
	        }

	        return equipments;

	    } catch (SQLException e) {
	        System.out.println("!! SQL Exception !!\n" + e);
	        e.printStackTrace();
	    } finally {
	        db.closeDb();
	    }

	    return null;
	}

	/**
	 * Creates an ArrayList filled with the results of a search
	 * @param search String that the results are being filtered by
	 * @param type indicates if the parameter being searched for is the brand or the sku
	 * @return an ArrayList with the equipments resulting from the managers search
	 */
	//https://www.postgresql.org/docs/current/functions-matching.html
	public static ArrayList<Equipment> searchEquipments(String search, String type) {
	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet result = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();

	        if (type.equals("brand")) {
	            sqlQuery.append(" SELECT * FROM equipment WHERE brand ILIKE ? ORDER BY id ASC ");
	        } else {
	            sqlQuery.append(" SELECT * FROM equipment WHERE sku ILIKE ? ORDER BY id ASC ");
	        }

	        ps = db.getConn().prepareStatement(sqlQuery.toString());
	        ps.clearParameters();
	        ps.setString(1, "%" + search + "%");

	        result = ps.executeQuery();

	        ArrayList<Equipment> filteredEquipments = new ArrayList<Equipment>();

	        while (result.next()) {

	            Equipment equipment = new Equipment(
	                    result.getInt("id"),
	                    result.getInt("client_id"),
	                    result.getString("brand"),
	                    result.getString("model"),
	                    result.getString("sku"),
	                    result.getString("batch"),
	                    result.getLong("manufacture_date"),
	                    result.getLong("submission_date"),
	                    (Long) result.getObject("repair_date")
	            );

	            filteredEquipments.add(equipment);
	        }

	        return filteredEquipments;

	    } catch (SQLException e) {
	        System.out.println("!! SQL Exception !!\n" + e);
	        e.printStackTrace();
	    } finally {
	        db.closeDb();
	    }

	    return null;
	}

	/**
	 * Query to get 10 elements from all of the available equipments
	 * @param i parameter used to indicate the page
	 * @return an ArrayList with 10 elements from the equipment table
	 */
	public static ArrayList<Equipment> listAllEquipments(Integer i) {

	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet result = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();
	        sqlQuery.append(" SELECT * FROM equipment LIMIT 10 OFFSET ? ");

	        ps = db.getConn().prepareStatement(sqlQuery.toString());
	        ps.clearParameters();
	        ps.setInt(1, 10 * i);

	        result = ps.executeQuery();

	        ArrayList<Equipment> equipments = new ArrayList<Equipment>();

	        while (result.next()) {

	            Equipment equipment = new Equipment(
	                    result.getInt("id"),
	                    result.getInt("client_id"),
	                    result.getString("brand"),
	                    result.getString("model"),
	                    result.getString("sku"),
	                    result.getString("batch"),
	                    result.getLong("manufacture_date"),
	                    result.getLong("submission_date"),
	                    (Long) result.getObject("repair_date")
	            );

	            equipments.add(equipment);
	        }

	        return equipments;

	    } catch (SQLException e) {
	        System.out.println("!! SQL Exception !!\n" + e);
	        e.printStackTrace();
	    } finally {
	        db.closeDb();
	    }

	    return null;
	}

	/**
	 * Query to get 10 elements from a certain clients equipments search
	 * @param search String being searched in the equipent table
	 * @param type if the search is being made by brand or sku
	 * @param clientId owner of the equipments that will be listed from the search
	 * @return an ArrayList with all the results from a clients equipment search
	 */
	//https://www.postgresql.org/docs/current/functions-matching.html
	public static ArrayList<Equipment> searchEquipmentsByClient(String search, String type, int clientId) {
	    DataBase db = new DataBase();
	    PreparedStatement ps = null;
	    ResultSet result = null;

	    try {
	        StringBuffer sqlQuery = new StringBuffer();

	        if (type.equals("brand")) {
	            sqlQuery.append(" SELECT * FROM equipment WHERE client_id = ? AND brand ILIKE ? ORDER BY id ASC ");
	        } else {
	            sqlQuery.append(" SELECT * FROM equipment WHERE client_id = ? AND sku ILIKE ? ORDER BY id ASC ");
	        }

	        ps = db.getConn().prepareStatement(sqlQuery.toString());
	        ps.clearParameters();
	        ps.setInt(1, clientId);
	        ps.setString(2, "%" + search + "%");

	        result = ps.executeQuery();

	        ArrayList<Equipment> filteredEquipments = new ArrayList<Equipment>();

	        while (result.next()) {

	            Equipment equipment = new Equipment(
	                    result.getInt("id"),
	                    result.getInt("client_id"),
	                    result.getString("brand"),
	                    result.getString("model"),
	                    result.getString("sku"),
	                    result.getString("batch"),
	                    result.getLong("manufacture_date"),
	                    result.getLong("submission_date"),
	                    (Long) result.getObject("repair_date")
	            );

	            filteredEquipments.add(equipment);
	        }

	        return filteredEquipments;

	    } catch (SQLException e) {
	        System.out.println("!! SQL Exception !!\n" + e);
	        e.printStackTrace();
	    } finally {
	        db.closeDb();
	    }

	    return null;
	}
	
}
