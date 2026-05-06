package controller;

import model.DataBase;
import model.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to manage Tests made during Repairs
 */
public class TestController {

    /**
     * Allows for the adding a new test to a repair, by the creation and execution of a prepared statement
     * @param test test data that will be added to the db
     * @return Confirmation that the test was or not added to the database
     */
    public static Boolean addTest(Test test) {
        DataBase db = new DataBase();
        PreparedStatement ps = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" INSERT INTO test (employee_id, repair_id, designation, description, test_date) ");
            sqlQuery.append(" VALUES (?, ?, ?, ?, ?) ");

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();
            ps.setInt(1, test.getEmployeeId());
            ps.setInt(2, test.getRepairId());
            ps.setString(3, test.getDesignation());
            ps.setString(4, test.getDescription());
            ps.setLong(5, test.getTestDate());

            ps.executeUpdate();

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
     * Shows all of the testes done during a repair
     * @param repairId repairs in question
     */
    public static void listTestsByRepair(int repairId) {
        DataBase db = new DataBase();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" SELECT designation, description, test_date ");
            sqlQuery.append(" FROM test ");
            sqlQuery.append(" WHERE repair_id = ? ");

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();
            ps.setInt(1, repairId);

            rs = ps.executeQuery();

            System.out.println("\nTests for repair ID: " + repairId);

            Boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Designation: " + rs.getString("designation"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Date: " + rs.getLong("test_date"));
                System.out.println("--------");
            }

            if (!found) {
                System.out.println("No tests found.");
            }

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n" + e);
            e.printStackTrace();
        } finally {
            db.closeDb();
        }
    }
}
