package controller;
import model.Client;
import model.DataBase;
import model.Employee;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to manage Employee resources
 */
public class EmployeeController {

    /**
     * Allows for the creation of a new Employee, by the creation and execution of a prepared statement
     * @param employee employee data that will be added to the db
     * @return Confirmation that the employee was or not added to the database
     */
    public static Boolean addEmployee(Employee employee){

        DataBase db = new DataBase();
        ResultSet result = null;
        try {
            StringBuffer insertAccountQuery = new StringBuffer();
            insertAccountQuery.append(" INSERT INTO account (name, username, password, active, approved, email, type, pic) ");
            insertAccountQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");
            PreparedStatement ps = db.getConn().prepareStatement(insertAccountQuery.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.clearParameters();
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getUsername());
            ps.setString(3, employee.getPassword());
            ps.setBoolean(4, employee.getActive());
            ps.setBoolean(5, employee.getApproved());
            ps.setString(6, employee.getEmail());
            ps.setString(7, employee.getType());
            ps.setString(8, employee.getPic());
            ps.executeUpdate();

            result = ps.getGeneratedKeys();

            while (result.next()) {
                int id = result.getInt("id");
                employee.setId(id);
            }

        } catch (SQLException e) {
            //System.out.println("ERROR CODE: "+ e.getSQLState());
            if (e.getSQLState().equals("23505")) {
                System.out.println("Username or Email already in use!");
            } else {
                System.out.println("!! SQL Exception !!\n" + e);
                e.printStackTrace();
            }
        }

        try {
            StringBuffer insertEmployeeQuery = new StringBuffer();
            insertEmployeeQuery.append(" INSERT INTO employee (account_id, nif, phone_number, residence, lvl_expertise, start_date) ");
            insertEmployeeQuery.append(" VALUES (?, ?, ?, ?, ?, ?) ");
            PreparedStatement ps= db.getConn().prepareStatement(insertEmployeeQuery.toString());
            ps.clearParameters();
            ps.setInt(1, employee.getId());
            ps.setString(2, employee.getNif());
            ps.setString(3, employee.getPhoneNumber());
            ps.setString(4, employee.getResidence());
            ps.setString(5, employee.getLvlExpertise());
            ps.setLong(6, employee.getStartDate());
            ps.executeUpdate();

            NotificationController.createAccountNotification(employee.getName(),employee.getId());

            return true;

        } catch (SQLException e) {
            //System.out.println("ERROR CODE: "+ e.getSQLState());
            if (e.getSQLState().equals("23505")) {
                System.out.println("NIF already in use!");
            } else {
                System.out.println("!! SQL Exception !!\n" + e);
                e.printStackTrace();
            }
        }

        db.closeDb();
        return false;
    }

    /**
     * Fetches an employee from the database based on an id
     * @param userId id of the employee we whant the data of
     * @return Object Employee containing all important information or null if failed
     */
    public static Employee getEmployee(Integer userId){
        DataBase db = new DataBase();

        try {
            StringBuffer getEmployeeData = new StringBuffer();
            getEmployeeData.append("SELECT * FROM account Right JOIN employee ON account.id  = employee.account_id WHERE id = ? ");
            PreparedStatement ps = db.getConn().prepareStatement(getEmployeeData.toString());
            ps.clearParameters();
            ps.setInt(1, userId);

            ResultSet result = ps.executeQuery();

            if (result.next()) {

                Employee employee = new Employee(
                        userId,
                        result.getString("name"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getBoolean("active"),
                        result.getBoolean("approved"),
                        result.getString("email"),
                        result.getString("pic"),
                        result.getString("type"),
                        result.getString("nif"),
                        result.getString("phone_number"),
                        result.getString("residence"),
                        result.getString("lvl_expertise"),
                        result.getLong("start_date")
                );

                return employee;
            }

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        db.closeDb();
        return null;
    }

    /**
     * Allows for the update of an employee data
     * @param employee employee data that will be added to the db
     * @return Confirmation that the employees data was or not update to the database
     */
    public static Boolean updateEmployee(Employee employee){
        DataBase db = new DataBase();

        try {
            StringBuffer updateAccount = new StringBuffer();
            updateAccount.append("UPDATE account " +
                    "SET name = ?, " +
                    "username = ?, " +
                    "password = ?, " +
                    "email = ?, " +
                    "active = ?, " +
                    "approved = ?, " +
                    "pic = ?" +
                    "WHERE id = ?;" +
                    "UPDATE employee " +
                    "SET nif = ?, " +
                    "phone_number = ?, " +
                    "residence = ?, " +
                    "lvl_expertise = ? " +
                    "WHERE account_id = ? ");
            PreparedStatement ps = db.getConn().prepareStatement(updateAccount.toString());
            ps.clearParameters();
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getUsername());
            ps.setString(3, employee.getPassword());
            ps.setString(4, employee.getEmail());
            ps.setBoolean(5, employee.getActive());
            ps.setBoolean(6, employee.getApproved());
            ps.setString(7, employee.getPic());
            ps.setInt(8, employee.getId());
            ps.setString(9, employee.getNif());
            ps.setString(10, employee.getPhoneNumber());
            ps.setString(11, employee.getResidence());
            ps.setString(12,employee.getLvlExpertise());
            ps.setInt(13, employee.getId());
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("New value is already in use!");
            } else {
                System.out.println("!! SQL Exception !!\n" + e);
                e.printStackTrace();
            }
        }finally {
            db.closeDb();
        }
        return false;
    }

    /**
     * Verifies if an employe exists given an employeeId
     * @param employeeId id of the employee that is being verified
     * @return Confirmation that the employee is present in the db
     */
    public static Boolean existsEmployee(int employeeId) {
        DataBase db = new DataBase();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" SELECT id ");
            sqlQuery.append(" FROM account ");
            sqlQuery.append(" WHERE id = ? AND type = ? ");

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();
            ps.setInt(1, employeeId);
            ps.setString(2, "employee");

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
     * Allows an employee to ask for its information omitted from the db
     * @param employeeId id of the employee to be deleted
     * @return Confirmation that the employees data was omitted from the database
     */
    public static Boolean deleteEmployee(Integer employeeId){
        DataBase db = new DataBase();

        try {
            StringBuffer updateAccount = new StringBuffer();
            updateAccount.append("UPDATE account " +
                    "SET active = ?, " +
                    "WHERE id = ?;" +
                    "UPDATE employee " +
                    "SET nif = ?, " +
                    "phone_number = ?, " +
                    "residence = ?, " +
                    "WHERE account_id = ? ");
            PreparedStatement ps = db.getConn().prepareStatement(updateAccount.toString());
            ps.clearParameters();
            ps.setBoolean(1, false);
            ps.setInt(2, employeeId);
            ps.setString(3, "000000000");
            ps.setString(4, "999999999");
            ps.setString(5, "---------");
            ps.setInt(6, employeeId);
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }finally {
            db.closeDb();
        }
        return false;
    }

    /**
     * Verifies if an employee was already assigned to a repair and declined it
     * @param employeeId employee being verified
     * @param repairId repair that the employee is trying to be assigned to
     * @return Confirmation if the employee is available to be assigned to a repair
     */
    public static Boolean availableForRepair(Integer employeeId, Integer repairId) {
        DataBase db = new DataBase();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();
            sqlQuery.append(" SELECT * ");
            sqlQuery.append(" FROM account ");
            sqlQuery.append(" WHERE type = 'employee' AND NOT EXISTS( ");
            sqlQuery.append(" SELECT * ");
            sqlQuery.append(" FROM notification ");
            sqlQuery.append(" WHERE notification.sender_id = ? ");
            sqlQuery.append(" AND notification.repair_id = ?) ");

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();
            ps.setInt(1, employeeId);
            ps.setInt(2, repairId);

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
}
