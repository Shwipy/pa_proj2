package controller;

import model.DataBase;
import model.Part;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * Class to manage Parts Resources
 */
public class PartController {

    /**
     * Allows for the creation of a new Part, by the creation and execution of a prepared statement
     * @param part part data that will be added to the db
     * @return Confirmation that the part was or not added to the database
     */
    public static Boolean addPart(Part part) {

        DataBase db = new DataBase();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" INSERT INTO part (employee_id, code, designation, manufacturer, amount) ");
            sqlQuery.append(" VALUES (?, ?, ?, ?, ?) ");

            ps = db.getConn().prepareStatement(sqlQuery.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.clearParameters();

            ps.setNull(1, Types.INTEGER);
            ps.setString(2, part.getCode());
            ps.setString(3, part.getDesignation());
            ps.setString(4, part.getManufacturer());
            ps.setInt(5, part.getAmount());

            ps.executeUpdate();

            rs = ps.getGeneratedKeys();

            while (rs.next()) {
                part.setId(rs.getInt(1));
            }

            return true;

        } catch (SQLException e) {
            System.out.println("ERROR CODE: " + e.getSQLState());

            if (e.getSQLState().equals("23505")) {
                System.out.println("Part code already exists.");
            } else {
                System.out.println("!! SQL Exception !!\n" + e);
                e.printStackTrace();
            }
        } finally {
            db.closeDb();
        }

        return false;
    }

    /**
     * Displays the list of all parts in the database
     */
    public static void listParts() {

        DataBase db = new DataBase();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" SELECT id, code, designation, manufacturer, amount ");
            sqlQuery.append(" FROM part ");
            sqlQuery.append(" ORDER BY id ASC ");

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();

            rs = ps.executeQuery();

            System.out.println("\nParts:");

            Boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Part ID: " + rs.getInt("id"));
                System.out.println("Code: " + rs.getString("code"));
                System.out.println("Designation: " + rs.getString("designation"));
                System.out.println("Manufacturer: " + rs.getString("manufacturer"));
                System.out.println("Amount: " + rs.getInt("amount"));
                System.out.println("--------");
            }

            if (!found) {
                System.out.println("No parts found.");
            }

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n" + e);
            e.printStackTrace();
        } finally {
            db.closeDb();
        }
    }

    /**
     * Verifies if the inputted part id exists in the databse
     * @param partId id of the part in question
     * @return Confirmation that the part exists or not
     */
    public static Boolean existsPart(int partId) {

        DataBase db = new DataBase();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" SELECT id ");
            sqlQuery.append(" FROM part ");
            sqlQuery.append(" WHERE id = ? ");

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();
            ps.setInt(1, partId);

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
     * Allows for the update of the amount of a specific part, by the creation and execution of a prepared statement
     * @param partId id of the part being updated
     * @param newAmount new amount of the part to be put in the db
     * @return Confirmation that the amount was or not updated
     */
    public static Boolean updatePartAmount(int partId, int newAmount) {

        DataBase db = new DataBase();
        PreparedStatement ps = null;

        try {
            if (newAmount < 0) {
                System.out.println("Amount cannot be negative.");
                return false;
            }

            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" UPDATE part ");
            sqlQuery.append(" SET amount = ? ");
            sqlQuery.append(" WHERE id = ? ");

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();
            ps.setInt(1, newAmount);
            ps.setInt(2, partId);

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
}
