package controller;

import model.DataBase;
import model.Manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to manage application access and account validation
 */
public class SessionController {

    /**
     * Allows for the creation of a new Manager, by the creation and execution of a prepared statement
     * @return true if there are users in th db or false if there aren't
     */
    public static Boolean hasUsers(){

        try {
            DataBase db = new DataBase();

            ResultSet result = db.getSt().executeQuery("SELECT * FROM account");

            if (result == null || !result.next()) {
                System.out.println("!! No Record on table !!");
                return false;
            }

            return true;
        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Function used to compare login credentials to account details when a user tryes to login;
     * Verifies if the account is active or if it was banned
     * @param username username being inputted during login
     * @param password password being inputted during login
     * @return returns the id of the user that logged in if the login was successfull or null if not
     */
    public static Integer login(String username, String password){

        DataBase db = new DataBase();

        try {
            StringBuffer getData = new StringBuffer();
            getData.append(" SELECT id,name,password,approved,active FROM account WHERE username = ? ");
            PreparedStatement ps = db.getConn().prepareStatement(getData.toString());
            ps.clearParameters();
            ps.setString(1, username);

            ResultSet result = ps.executeQuery();

            if (result.next()) {

                if (!result.getBoolean("approved")){
                    System.out.println("Conta em analise!");
                    return null;
                }
                if (!result.getBoolean("active")){
                    System.out.println("Conta encontra-se desativada!");
                    return null;
                }
                Integer id = result.getInt("id");
                String name = result.getString("name");
                String pass = result.getString("password");

                if (password.equals(pass)) {
                    return id;
                } else {
                    System.out.println(" !! Wrong Password !!");
                    return null;
                }
            }
            System.out.println(" !! User not Found !!");
            return null;

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        db.closeDb();
        return null;
    }

    /**
     * Function used to get an account type from its id
     * @param id id of the account we want to know the type of
     * @return a string with the type of the account in question
     */
    public static String getAccountType(Integer id){

        DataBase db = new DataBase();

        try {
            StringBuffer getType = new StringBuffer();
            getType.append(" SELECT type FROM account WHERE id = ? ");
            PreparedStatement ps = db.getConn().prepareStatement(getType.toString());
            ps.clearParameters();
            ps.setInt(1, id);

            ResultSet result = ps.executeQuery();

            if (result.next()) {
                return result.getString("type");
            }
            

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        db.closeDb();
        return null;
    }

    /**
     * Function keep logs of the number of execution and date of the last execution
     */
    public static void addLogExecution(){
        DataBase db = new DataBase();
        Integer numExec = null;
        String todayFormattedData = OutputController.dateFormater(System.currentTimeMillis() / 1000, "log");

        try {

            ResultSet result = db.getSt().executeQuery("SELECT * FROM syslog");

            if (result == null || !result.next()) {
                //no data on table
                StringBuffer insertFirstExecution = new StringBuffer();
                insertFirstExecution.append(" INSERT INTO syslog (last_exec, numb_exec) ");
                insertFirstExecution.append(" VALUES (?, ?) ");
                PreparedStatement ps = db.getConn().prepareStatement(insertFirstExecution.toString());
                ps.clearParameters();
                ps.setString(1, todayFormattedData);
                ps.setInt(2, 1);
                ps.executeUpdate();
            }else{
                numExec = result.getInt("numb_exec");
                StringBuffer insertNewLog = new StringBuffer();
                insertNewLog.append(" Update syslog SET last_exec=?, numb_exec=? ");
                PreparedStatement ps = db.getConn().prepareStatement(insertNewLog.toString());
                ps.clearParameters();
                ps.setString(1,todayFormattedData);
                ps.setInt(2, numExec+1);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n" + e);
            e.printStackTrace();
        }finally {
            db.closeDb();

        }
    }
}
