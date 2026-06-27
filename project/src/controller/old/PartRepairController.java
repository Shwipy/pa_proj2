package controller;
import model.DataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to manage Parts used in Repairs
 */
public class PartRepairController {

    /**
     * Creates an entry in the database to relate which part was used on which repair and the amount
     * @param repairId repair in question
     * @param partId id of the part that was used
     * @param amountUsed amount of the part that was used in the repair
     * @return Confirmation that the entry was or not added to the database
     */
    public static Boolean addPartToRepair(int repairId, int partId, int amountUsed) {

        DataBase db = new DataBase();
        Connection conn = null;
        PreparedStatement psCheckRepair = null;
        PreparedStatement psCheckPart = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        ResultSet rsRepair = null;
        ResultSet rsPart = null;

        try {
            if (amountUsed <= 0) {
                System.out.println("Amount used must be greater than zero.");
                return false;
            }

            conn = db.getConn();
            conn.setAutoCommit(false);

            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" SELECT id ");
            sqlQuery.append(" FROM repair ");
            sqlQuery.append(" WHERE id = ? AND state = ? ");

            psCheckRepair = conn.prepareStatement(sqlQuery.toString());
            psCheckRepair.clearParameters();
            psCheckRepair.setInt(1, repairId);
            psCheckRepair.setString(2, "in_progress");

            rsRepair = psCheckRepair.executeQuery();

            if (!rsRepair.next()) {
                System.out.println("Repair not found or not in progress.");
                conn.rollback();
                return false;
            }

            sqlQuery = new StringBuffer();
            sqlQuery.append(" SELECT amount ");
            sqlQuery.append(" FROM part ");
            sqlQuery.append(" WHERE id = ? ");

            psCheckPart = conn.prepareStatement(sqlQuery.toString());
            psCheckPart.clearParameters();
            psCheckPart.setInt(1, partId);

            rsPart = psCheckPart.executeQuery();

            if (!rsPart.next()) {
                System.out.println("Part not found.");
                conn.rollback();
                return false;
            }

            int currentAmount = rsPart.getInt("amount");

            if (currentAmount < amountUsed) {
                System.out.println("Insufficient stock. Current amount: " + currentAmount);
                conn.rollback();
                return false;
            }

            sqlQuery = new StringBuffer();
            sqlQuery.append(" INSERT INTO part_repair (repair_id, part_id, amount_used) ");
            sqlQuery.append(" VALUES (?, ?, ?) ");

            psInsert = conn.prepareStatement(sqlQuery.toString());
            psInsert.clearParameters();
            psInsert.setInt(1, repairId);
            psInsert.setInt(2, partId);
            psInsert.setInt(3, amountUsed);

            int insertedRows = psInsert.executeUpdate();

            if (insertedRows <= 0) {
                System.out.println("Could not register part in repair.");
                conn.rollback();
                return false;
            }

            sqlQuery = new StringBuffer();
            sqlQuery.append(" UPDATE part ");
            sqlQuery.append(" SET amount = amount - ? ");
            sqlQuery.append(" WHERE id = ? ");

            psUpdate = conn.prepareStatement(sqlQuery.toString());
            psUpdate.clearParameters();
            psUpdate.setInt(1, amountUsed);
            psUpdate.setInt(2, partId);

            int updatedRows = psUpdate.executeUpdate();

            if (updatedRows <= 0) {
                System.out.println("Could not update part stock.");
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n" + e);
            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("!! SQL Exception !!\n" + ex);
                    ex.printStackTrace();
                }
            }
        } finally {
            db.closeDb();
        }

        return false;
    }

    /**
     * Allows the listing of the parts that were used during a repari
     * @param repairId repair in question
     */
    public static void listPartsByRepair(int repairId) {

        DataBase db = new DataBase();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" SELECT part.id, part.code, part.designation, part.manufacturer, part_repair.amount_used ");
            sqlQuery.append(" FROM part, part_repair ");
            sqlQuery.append(" WHERE part.id = part_repair.part_id ");
            sqlQuery.append(" AND part_repair.repair_id = ? ");

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();
            ps.setInt(1, repairId);

            rs = ps.executeQuery();

            System.out.println("\nParts for Repair ID: " + repairId);

            Boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("Part ID: " + rs.getInt("id"));
                System.out.println("Code: " + rs.getString("code"));
                System.out.println("Designation: " + rs.getString("designation"));
                System.out.println("Manufacturer: " + rs.getString("manufacturer"));
                System.out.println("Amount Used: " + rs.getInt("amount_used"));
                System.out.println("--------");
            }

            if (!found) {
                System.out.println("No parts associated with this repair.");
            }

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n" + e);
            e.printStackTrace();
        } finally {
            db.closeDb();
        }
    }
}
