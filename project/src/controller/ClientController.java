package controller;

import model.Client;
import model.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to manage Client resources
 */
public class ClientController {


    /**
     * Allows for the creation of a new Client, by the creation and execution of a prepared statement
     * @param client client data that will be added to the db
     * @return Confirmation that the client was or not added to the database
     */
    public static Boolean addClient(Client client){

        DataBase db = new DataBase();
        ResultSet result = null;
        try {
            //https://www.geeksforgeeks.org/java/how-to-get-the-insert-id-in-jdbc/
            StringBuffer insertAccountQuery = new StringBuffer();
            insertAccountQuery.append(" INSERT INTO account (name, username, password, active, approved, email, type, pic) ");
            insertAccountQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");
            PreparedStatement ps = db.getConn().prepareStatement(insertAccountQuery.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.clearParameters();
            ps.setString(1, client.getName());
            ps.setString(2, client.getUsername());
            ps.setString(3, client.getPassword());
            ps.setBoolean(4, client.getActive());
            ps.setBoolean(5, client.getApproved());
            ps.setString(6, client.getEmail());
            ps.setString(7, client.getType());
            ps.setString(8, client.getPic());
            ps.executeUpdate();

            result = ps.getGeneratedKeys();

            while (result.next()) {
                int id = result.getInt("id");
                client.setId(id);
            }
            
        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n" + e);
            e.printStackTrace();
        }

            try{
            StringBuffer insertClientQuery = new StringBuffer();
            insertClientQuery.append(" INSERT INTO client (account_id, nif, phone_number, residence, business_sector, tier) ");
            insertClientQuery.append(" VALUES (?, ?, ?, ?, ?, ?) ");
            PreparedStatement ps = db.getConn().prepareStatement(insertClientQuery.toString());
            ps.clearParameters();
            ps.setInt(1, client.getId());
            ps.setString(2, client.getNif());
            ps.setString(3, client.getPhoneNumber());
            ps.setString(4, client.getResidence());
            ps.setString(5, client.getBusinessSector());
            ps.setString(6, client.getTier());
            ps.executeUpdate();

            NotificationController.createAccountNotification(client.getName(),client.getId());
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
     * Fetches a client from the database based on an id
     * @param userId id of the client we want the data of
     * @return Object Client containing all important information or null if failed
     */
    public static Client getClient(Integer userId){
        DataBase db = new DataBase();

        try {
            StringBuffer getClientData = new StringBuffer();
            getClientData.append("SELECT * FROM account Right JOIN client ON account.id  = client.account_id WHERE id = ? ");
            PreparedStatement ps = db.getConn().prepareStatement(getClientData.toString());
            ps.clearParameters();
            ps.setInt(1, userId);

            ResultSet result = ps.executeQuery();

            if (result.next()) {

                Client client = new Client(
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
                        result.getString("business_sector"),
                        result.getString("tier")
                );

                return client;
            }

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        db.closeDb();
        return null;
    }

    /**
     * Allows for the update of a client
     * @param client client data that will be added to the db
     * @return Confirmation that the client data was or not updated to the database
     */
    public static Boolean updateClient(Client client){
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
                    "pic = ? " +
                    "WHERE id = ?;" +
                    "UPDATE client " +
                    "SET nif = ?, " +
                    "phone_number = ?, " +
                    "residence = ?, " +
                    "business_sector = ? " +
                    "WHERE account_id = ? ");
            PreparedStatement ps = db.getConn().prepareStatement(updateAccount.toString());
            ps.clearParameters();
            ps.setString(1, client.getName());
            ps.setString(2, client.getUsername());
            ps.setString(3, client.getPassword());
            ps.setString(4, client.getEmail());
            ps.setBoolean(5, client.getActive());
            ps.setBoolean(6, client.getApproved());
            ps.setString(7, client.getPic());
            ps.setInt(8, client.getId());
            ps.setString(9, client.getNif());
            ps.setString(10, client.getPhoneNumber());
            ps.setString(11, client.getResidence());
            ps.setString(12, client.getBusinessSector());
            ps.setInt(13, client.getId());
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("The value is already in use!");
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
     * Allows a client to ask for its information omitted from the db
     * @param clientId id of the client to be deleted
     * @return Confirmation that the client data was omitted from the database
     */
    public static Boolean deleteClient(Integer clientId){
        DataBase db = new DataBase();

        try {
            StringBuffer updateAccount = new StringBuffer();
            updateAccount.append("UPDATE account " +
                    "SET active = ? " +
                    "WHERE id = ?;" +
                    "UPDATE client " +
                    "SET nif = ?, " +
                    "phone_number = ?, " +
                    "residence = ?, " +
                    "business_sector = ?, " +
                    "tier = ? " +
                    "WHERE account_id = ? ");
            PreparedStatement ps = db.getConn().prepareStatement(updateAccount.toString());
            ps.clearParameters();
            ps.setBoolean(1, false);
            ps.setInt(2, clientId);
            ps.setString(3, "000000000");
            ps.setString(4, "999999999");
            ps.setString(5, "---------");
            ps.setString(6,"---------");
            ps.setString(7,"-");
            ps.setInt(8, clientId);
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
}
