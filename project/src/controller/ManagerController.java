package controller;

import model.Client;
import model.DataBase;
import model.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to manage Manger and Account related resources
 */
public class ManagerController {

    /**
     * Allows for the creation of a new Manager, by the creation and execution of a prepared statement
     * @param manager manager data that will be added to the db
     * @return Confirmation that the manager was or not added to the database
     */
    public static Boolean addManager(Manager manager){
        DataBase db = new DataBase();

        try {
            StringBuffer insertAccountQuery = new StringBuffer();
            insertAccountQuery.append(" INSERT INTO account (name, username, password, active, approved, email, type) ");
            insertAccountQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?) ");
            PreparedStatement ps = db.getConn().prepareStatement(insertAccountQuery.toString());
            ps.clearParameters();
            ps.setString(1, manager.getName());
            ps.setString(2, manager.getUsername());
            ps.setString(3, manager.getPassword());
            ps.setBoolean(4, manager.getActive());
            ps.setBoolean(5, manager.getApproved());
            ps.setString(6, manager.getEmail());
            ps.setString(7, manager.getType());
            ps.executeUpdate();

            db.closeDb();
            return true;

        } catch (SQLException e) {
            //System.out.println("ERROR CODE: "+ e.getSQLState());
            if (e.getSQLState().equals("23505")) {
                System.out.println("New value is already in use!");
            } else {
                System.out.println("!! SQL Exception !!\n" + e);
                e.printStackTrace();
            }
        }

        db.closeDb();
        return false;
    }

    /**
     * Fetches a manager or basic account information from the database based on an id
     * @param id id of the manager or account to get their basic data from the db
     * @return Object Manager containing all important information or null if failed
     */
    public static Manager getManager(Integer id){
        DataBase db = new DataBase();

        try {
            StringBuffer getManager = new StringBuffer();
            getManager.append(" SELECT * FROM account WHERE id = ? ");
            PreparedStatement ps = db.getConn().prepareStatement(getManager.toString());
            ps.clearParameters();
            ps.setInt(1, id);

            ResultSet result = ps.executeQuery();

            if (result.next()) {

                Manager manager = new Manager(
                        id,
                        result.getString("name"),
                        result.getString("username"),
                        result.getBoolean("active"),
                        result.getBoolean("approved"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("type")
                        );

                return manager;
            }


        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        db.closeDb();
        return null;
    }

    /**
     * Allows for the update of a manager
     * @param manager manager data that will be added to the db
     * @return Confirmation that the manager data was or not updated to the database
     */
    public static Boolean updateManager(Manager manager){
        DataBase db = new DataBase();

        try {
            StringBuffer updateAccount = new StringBuffer();
            updateAccount.append("UPDATE account " +
                    "SET name = ?, " +
                    "username = ?, " +
                    "password = ?, " +
                    "email = ? " +
                    "WHERE id = ?" );
            PreparedStatement ps = db.getConn().prepareStatement(updateAccount.toString());
            ps.clearParameters();
            ps.setString(1, manager.getName());
            ps.setString(2, manager.getUsername());
            ps.setString(3, manager.getPassword());
            ps.setString(4, manager.getEmail());
            ps.setInt(5, manager.getId());

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
     * Allows for the listing in intervals of 10 values of all types of accounts
     * @param i variable referring to the paging
     * @param asc if the listing will be in and asc or desc order
     * @return an arraylist with the common data to 10 accounts
     */
//    public static ArrayList listUsers(Integer i, Boolean asc){
    public static ArrayList listUsers(){
        DataBase db = new DataBase();

        try {
            StringBuffer getAccouts = new StringBuffer();

            getAccouts.append(" SELECT * FROM account ORDER BY name");
            PreparedStatement ps = db.getConn().prepareStatement(getAccouts.toString());
            ps.clearParameters();

            ArrayList<Manager> users = new ArrayList<>();
            ResultSet result = ps.executeQuery();

            while (result.next()) {

                Manager account = new Manager(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("username"),
                        result.getBoolean("active"),
                        result.getBoolean("approved"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("type")
                );

                users.add(account);
            }
            return users;

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        db.closeDb();
        return null;
    }

    /**
     * Allows for the search of all entry's base on an inputted string
     * @param search string that will be searched in the db
     * @param type parameter we are filtering by in the database, name/username/type
     * @return result of the search on the db
     */
    //https://www.postgresql.org/docs/current/functions-matching.html
    public static ArrayList searchUsers(String search, String type){
        DataBase db = new DataBase();

        try {
            StringBuffer getAccouts = new StringBuffer();

            if (type.equals("name")) {
                getAccouts.append(" SELECT *  FROM account WHERE name ILIKE ? ORDER BY id ASC\n ");

            } else if (type.equals("username")) {
                getAccouts.append(" SELECT *  FROM account WHERE username ILIKE ? ORDER BY id ASC\n");

            }else{
                getAccouts.append(" SELECT *  FROM account WHERE type ILIKE ? ORDER BY id ASC\n");
            }


            PreparedStatement ps = db.getConn().prepareStatement(getAccouts.toString());
            ps.clearParameters();
            ps.setString(1,"%"+search+"%");

            ArrayList<Manager> filteredUsers = new ArrayList<>();
            ResultSet result = ps.executeQuery();

            while (result.next()) {

                Manager account = new Manager(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("username"),
                        result.getBoolean("active"),
                        result.getBoolean("approved"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("type")
                );

                filteredUsers.add(account);
            }

            return filteredUsers;

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        db.closeDb();
        return null;
    }

    /**
     * Allows a manager to activate a created account
     * @param id id of the client to be set as activate
     * @return Confirmation that the client was set to active
     */
    public static Boolean activateAccount(Integer id){
        DataBase db = new DataBase();

        try {
            StringBuffer approveAccount = new StringBuffer();
            approveAccount.append("UPDATE account " +
                    "SET approved = true " +
                    "WHERE id = ?" );
            PreparedStatement ps = db.getConn().prepareStatement(approveAccount.toString());
            ps.clearParameters();
            ps.setInt(1, id);

            //https://www.geeksforgeeks.org/java/difference-between-execute-query-and-update-methods-in-java/
            int count = ps.executeUpdate();

            if (count == 0){
                return false;
            }

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
     * Allows a manager to ban/unban an account
     * @param id id of the account to be banned/unbanned
     * @param action specifies if the account is getting banned or unbanned
     * @return Confirmation that the client was banned or unbanned with success
     */
    public static Boolean banAccount(Integer id, String action){
        DataBase db = new DataBase();

        try {
            StringBuffer banAccount = new StringBuffer();

            if (action.equals("ban")) {
                banAccount.append("UPDATE account " +
                        "SET active = false " +
                        "WHERE id = ?");
            }else {
                banAccount.append("UPDATE account " +
                        "SET active = true " +
                        "WHERE id = ?");
            }
            PreparedStatement ps = db.getConn().prepareStatement(banAccount.toString());
            ps.clearParameters();
            ps.setInt(1, id);
            int count = ps.executeUpdate();

            if (count == 0){
                return false;
            }

            return true;

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }finally {
            db.closeDb();
        }
        return false;
    }
}

