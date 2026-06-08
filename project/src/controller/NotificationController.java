package controller;
import model.Client;
import model.DataBase;
import model.Manager;
import model.Notification;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class to manage Notification resources
 */
public class NotificationController {

    /**
     * Creates a notification referring to a newly created account
     * Sent from a client to all manager
     * @param username username of the newly created account
     * @param id id of the newly created account
     */
    public static void createAccountNotification(String username,Integer id){
        Notification notification = new Notification(
                -1,
                null,
                "Do you want to approve User [" + username + "]?",
                false,
                System.currentTimeMillis()/1000,
                id,
                "newAccount",
                null
        );
        sendNotification(notification);
    }

    /**
     * Creates a notification referring to the removal of an account
     * * Sent from a client or employee to all manager
     * @param username username of the account to be removed
     * @param id id of the account to be removed
     */
    public static void deleteAccountNotification(String username,Integer id){
        Notification notification = new Notification(
                -1,
                null,
                "User [" + username + "] wants their account deleted.",
                false,
                System.currentTimeMillis()/1000,
                id,
                "deleteAccount",
                null
        );
        sendNotification(notification);
    }

    /**
     * Creates a notification referring to the creation of a new repair
     * * Sent from a client to all manager
     * @param repairNumber number of the repair being created
     * @param repairId id of the repair being created
     * @param clientId id of the client that created the repair request
     */
    public static void createRepairNotification(String repairNumber, Integer repairId, Integer clientId){
        Notification notification = new Notification(
                -1,
                null, 
                "Do you want to approve Repair [" + repairNumber + "]?",
                false,
                System.currentTimeMillis()/1000,
                clientId, 
                "newRepair",
                repairId
        );
        sendNotification(notification);
    }

    /**
     * Creates a notification assigning a repair to an employee
     * Sent from a manager to an employee
     * @param repairId id of the repair that's being assigned
     * @param  employeeId id of the employee that's being assigned to the repair
     */
    public static void assignRepairNotification(Integer repairId, Integer employeeId){
        Notification notification = new Notification(
                -1,
                employeeId,
                "You have been assign a Repair, do you accept?",
                false,
                System.currentTimeMillis()/1000,
                null,
                "assignEmployee",
                repairId
        );
        sendNotification(notification);
    }

    /**
     * Creates a notification informing all managers that an employee rejected being assigned to a repair
     * Sent from a employee to all managers
     * @param employeeId id of the employee that's rejecting the repair
     * @param repairId id of the repair that's being rejected
     */
    public static void dennyRepair(Integer employeeId, Integer repairId){
        Notification notification = new Notification(
                -1,
                null,
                "Employee has rejected the repair",
                false,
                System.currentTimeMillis()/1000,
                employeeId,
                "reassignEmployee",
                repairId
        );
        sendNotification(notification);
    }

    /**
     * Creates a notification informing a client that a manager rejected the repair
     * Sent from a manager to a client
     * @param clientId id of the client that created the repair request
     * @param repairId id of the repair that's being rejected
     */
    public static void rejectRepairNotification(Integer clientId, Integer repairId){
        Notification notification = new Notification(
                -1,
                clientId,
                "Your Repair with the ID [" + repairId + "] was rejected and archived.",
                false,
                System.currentTimeMillis()/1000,
                null,
                "repairRejected",
                repairId
        );
        sendNotification(notification);
    }

    /**
     * Creates a notification informing a client that their repair was finished
     * Sent from a manager to a client
     * @param clientId id of the client that requested the repair
     * @param repairId id of the repair that finished
     */
    public static void finishRepairNotification(Integer clientId, Integer repairId){
        Notification notification = new Notification(
                -1,
                clientId,
                "Your Repair with the ID [" + repairId + "] was finished.",
                false,
                System.currentTimeMillis()/1000,
                null,
                "repairFinished",
                repairId
        );
        sendNotification(notification);
    }

    /**
     * Allows for the creation of a new notification, by the creation and execution of a prepared statement
     * @param notification notification data that will be added to the db
     * @return Confirmation that the notification was or not added to the database(sent)
     */
    public static Boolean sendNotification(Notification notification){
        DataBase db = new DataBase();

        try {
            StringBuffer insertAccountQuery = new StringBuffer();
            insertAccountQuery.append(" INSERT INTO notification (message, read , creation_date, account_id, sender_id, type, repair_id) ");
            insertAccountQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?) ");
            PreparedStatement ps = db.getConn().prepareStatement(insertAccountQuery.toString());
            ps.clearParameters();
            ps.setString(1, notification.getMessage());
            ps.setBoolean(2, notification.getIsRead());
            ps.setLong(3, notification.getCreationDate());
            ps.setObject(4, notification.getAccountId());
            ps.setObject(5, notification.getSenderId());
            ps.setString(6, notification.getType());
            ps.setObject(7, notification.getRepairId());
            ps.executeUpdate();

            db.closeDb();
            return true;

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n" + e);
            e.printStackTrace();
        }

        db.closeDb();
        return false;
    }

    /**
     * Fetches a notification from the database based on an id
     * This function is related to managers
     * @param id id of the notification we want the data of
     * @return Object notification containing all important information or null if failed
     */
    public static Notification getNotificationManager(Integer id){
        DataBase db = new DataBase();

        try {
            StringBuffer getNotificationManager = new StringBuffer();
            getNotificationManager.append(" SELECT * FROM notification WHERE read = false AND account_id IS NULL AND id=?");
            PreparedStatement ps = db.getConn().prepareStatement(getNotificationManager.toString());
            ps.clearParameters();
            ps.setInt(1, id);

            ResultSet result = ps.executeQuery();

            if (result.next()) {

                Notification notification = new Notification(
                        result.getInt("id"),
                        result.getInt("account_id"),
                        result.getString("message"),
                        result.getBoolean("read"),
                        result.getLong("creation_date"),
                        result.getInt("sender_id"),
                        result.getString("type"),
                        result.getInt("repair_id")
                );

                return notification;
            }

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        db.closeDb();
        return null;
    }

    /**
     * Fetches a notification from the database based on an id
     * This function is for employees and clients only
     * @param id id of the notification we want the data of
     * @param userID id of the user that's trying to get it
     * @return Object notification containing all important information or null if failed
     */
    public static Notification getNotification(Integer id, Integer userID){
        DataBase db = new DataBase();

        try {
            StringBuffer getNotificationManager = new StringBuffer();
            getNotificationManager.append(" SELECT * FROM notification WHERE read = false AND account_id =? AND id=?");
            PreparedStatement ps = db.getConn().prepareStatement(getNotificationManager.toString());
            ps.clearParameters();
            ps.setInt(1, userID);
            ps.setInt(2, id);

            ResultSet result = ps.executeQuery();

            if (result.next()) {

                Notification notification = new Notification(
                        result.getInt("id"),
                        result.getInt("account_id"),
                        result.getString("message"),
                        result.getBoolean("read"),
                        result.getLong("creation_date"),
                        result.getInt("sender_id"),
                        result.getString("type"),
                        result.getInt("repair_id")
                );

                return notification;
            }

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n"+e);
            e.printStackTrace();
        }
        db.closeDb();
        return null;
    }

    /**
     * Fetches all unready notifications that are meant for managers
     * @param i variable referring to the paging
     * @param asc if the listing will be in and asc or desc order
     * @return an arraylist with the information of 10 notifications
     */
   public static ArrayList<Notification> listNotificationsManager(Boolean showAll) {

        DataBase db = new DataBase();
        PreparedStatement ps = null;
        ResultSet result = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();

            sqlQuery.append(" SELECT * FROM notification ");
            sqlQuery.append(" WHERE account_id IS NULL ");

            if (showAll == false) {
                sqlQuery.append(" AND read = false ");
            }

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();

            result = ps.executeQuery();

            ArrayList<Notification> notifications = new ArrayList<Notification>();

            while (result.next()) {

                Integer accountId = null;
                Integer senderId = null;
                Integer repairId = null;

                //https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html#getObject-java.lang.String-
                Number accountIdValue = (Number) result.getObject("account_id");
                Number senderIdValue = (Number) result.getObject("sender_id");
                Number repairIdValue = (Number) result.getObject("repair_id");

                //https://docs.oracle.com/javase/8/docs/api/java/lang/Number.html#intValue--
                if (accountIdValue != null) {
                    accountId = accountIdValue.intValue();
                }

                if (senderIdValue != null) {
                    senderId = senderIdValue.intValue();
                }

                if (repairIdValue != null) {
                    repairId = repairIdValue.intValue();
                }

                Notification notification = new Notification(
                        result.getInt("id"),
                        accountId,
                        result.getString("message"),
                        result.getBoolean("read"),
                        result.getLong("creation_date"),
                        senderId,
                        result.getString("type"),
                        repairId
                );

                notifications.add(notification);
            }

            return notifications;

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n" + e);
            e.printStackTrace();
        } finally {
            db.closeDb();
        }

        return null;
    }


    /**
     * Fetches all unready notifications that are employees or clients based on id
     * @param i variable referring to the paging
     * @param asc if the listing will be in and asc or desc order
     * @param accountId id of the account that wants the notifications
     * @return an arraylist with the information of 10 notifications
     */
    public static ArrayList<Notification> listNotifications(Integer accountId) {

        DataBase db = new DataBase();
        PreparedStatement ps = null;
        ResultSet result = null;

        try {
            StringBuffer sqlQuery = new StringBuffer();

            sqlQuery.append(" SELECT * FROM notification ");
            sqlQuery.append(" WHERE read = false ");
            sqlQuery.append(" AND account_id = ? ");

            ps = db.getConn().prepareStatement(sqlQuery.toString());
            ps.clearParameters();
            ps.setInt(1, accountId);

            result = ps.executeQuery();

            ArrayList<Notification> notifications = new ArrayList<Notification>();

            while (result.next()) {

                Integer notificationAccountId = null;
                Integer senderId = null;
                Integer repairId = null;

                //https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html#getObject-java.lang.String-
                Number accountIdValue = (Number) result.getObject("account_id");
                Number senderIdValue = (Number) result.getObject("sender_id");
                Number repairIdValue = (Number) result.getObject("repair_id");

                //https://docs.oracle.com/javase/8/docs/api/java/lang/Number.html#intValue--
                if (accountIdValue != null) {
                    notificationAccountId = accountIdValue.intValue();
                }

                if (senderIdValue != null) {
                    senderId = senderIdValue.intValue();
                }

                if (repairIdValue != null) {
                    repairId = repairIdValue.intValue();
                }

                Notification notification = new Notification(
                        result.getInt("id"),
                        notificationAccountId,
                        result.getString("message"),
                        result.getBoolean("read"),
                        result.getLong("creation_date"),
                        senderId,
                        result.getString("type"),
                        repairId
                );

                notifications.add(notification);
            }

            return notifications;

        } catch (SQLException e) {
            System.out.println("!! SQL Exception !!\n" + e);
            e.printStackTrace();
        } finally {
            db.closeDb();
        }

        return null;
    }

    /**
     * Used to mark a notification as read so that it doesn't show up anymore on the menu
     * This function is called after the users finish interacting with the notification
     * @param notification notification in question
     * @return Indicates if the operation was or not successful
     */
    public static Boolean maskAsRead(Notification notification){
        DataBase db = new DataBase();

        try {
            StringBuffer updateNotification = new StringBuffer();
            updateNotification.append("UPDATE notification " +
                    "SET read = ? " +
                    "WHERE id = ?" );
            PreparedStatement ps = db.getConn().prepareStatement(updateNotification.toString());
            ps.clearParameters();
            ps.setBoolean(1, true);
            ps.setInt(2, notification.getId());

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
