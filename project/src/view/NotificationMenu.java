package view;
import controller.*;
import model.Manager;
import model.Notification;
import java.util.ArrayList;

/**
 * Displayed to console multiple menus to list, view and interact with notifications
 */
public class NotificationMenu {

    /**
     * Displays all notification for an account;
     * Implemented paging and a maximum of 10 entrys
     * @param accountId id of the account that is accessing the notifications
     */
    public static void showNotifications(Integer accountId) {

        Integer j = 0;
        Boolean asc = true;
        ArrayList<Notification> notifications;

        while (true) {
            System.out.println("\nNotifications:");
            if (SessionController.getAccountType(accountId).equals("manager")) {
                notifications = NotificationController.listNotificationsManager(j, asc);
            }else{
                notifications = NotificationController.listNotifications(j, asc, accountId);
            }


            for (Notification notification : notifications) {
                System.out.println(notification.toString());
            }

            System.out.println("['<'- Back      '>'- Next Page]");
            System.out.println("1- View Notification");

            System.out.println("2- Exit");

            switch (InputController.readString("> ")) {
                case ">":
                    j += 1;
                    if (SessionController.getAccountType(accountId).equals("manager")) {
                        notifications = NotificationController.listNotificationsManager(j, asc);
                    }else{
                        notifications = NotificationController.listNotifications(j, asc, accountId);
                    }

                    if (notifications.size() == 0) {
                        j -= 1;
                        if (SessionController.getAccountType(accountId).equals("manager")) {
                            notifications = NotificationController.listNotificationsManager(j, asc);
                        }else{
                            notifications = NotificationController.listNotifications(j, asc, accountId);
                        }
                        System.out.println("No more pages");
                    }
                    break;

                case "<":
                    if (j - 1 < 0) {
                        System.out.println("This is the last page");
                    } else {
                        j -= 1;
                        if (SessionController.getAccountType(accountId).equals("manager")) {
                            notifications = NotificationController.listNotificationsManager(j, asc);
                        }else{
                            notifications = NotificationController.listNotifications(j, asc, accountId);
                        }
                    }
                    break;

                case "1":
                    Notification notification = null;
                    if (SessionController.getAccountType(accountId).equals("manager")) {
                        notification = NotificationController.getNotificationManager(InputController.readInt("Notification ID>"));
                    }else{
                        notification = NotificationController.getNotification(InputController.readInt("Notification ID>"),accountId);
                    }


                    if (notification != null){
                        viewNotification(notification);
                    }
                    break;

                case "2":
                    return;

              
                default:
                    break;
            }
        }
    }

    /**
     * Displays the sub-options for each different type of notification;
     * Allows te user to interact with the notifications
     * @param notification object Notification that contains all the data of the notification that is being accessed
     */
    public static void viewNotification(Notification notification) {

        switch (notification.getType()){
            case "newAccount":
                while (true) {
                    System.out.println("\nDo you want to activate the account?");
                    System.out.println("(Yes/No)");

                    switch (InputController.readString("> ").trim().toLowerCase()) {
                        case "y", "yes":
                            if(ManagerController.activateAccount(notification.getSenderId())){
                                System.out.println("Account activated with success!");
                                NotificationController.maskAsRead(notification);
                            }else{
                                System.out.println("There was an error activating account");
                            }
                            return;

                        case "n", "no":
                            return;

                        default:
                            System.out.println("Invalid Option.");
                            break;
                    }
                }

            case "deleteAccount":

                String accountType = SessionController.getAccountType(notification.getSenderId());
                while (true) {
                    System.out.println("\nDo you want to delete the account?");
                    System.out.println("(Yes/No)");

                    switch (InputController.readString("> ").trim().toLowerCase()) {
                        case "y", "yes":

                            if (accountType.equals("client")){
                                if(ClientController.deleteClient(notification.getSenderId())){
                                    System.out.println("Account deleted with success!");
                                    NotificationController.maskAsRead(notification);
                                }else{
                                    System.out.println("There was an error deleting the account");
                                }
                                return;
                            } else if (accountType.equals("employee")) {
                                if(EmployeeController.deleteEmployee(notification.getSenderId())){
                                    System.out.println("Account deleted with success!");
                                    NotificationController.maskAsRead(notification);
                                }else{
                                    System.out.println("There was an error deleting the account account");
                                }
                                return;
                            }

                        case "n", "no":
                            return;

                        default:
                            System.out.println("Invalid Option.");
                            break;
                    }
                }

            case "newRepair":

                while (true) {
                    System.out.println("\nDo you want to approve the repair?");
                    System.out.println("(Yes/No)");

                    switch (InputController.readString("> ").trim().toLowerCase()) {

                        case "y", "yes":

                            if (!RepairController.existsSubmittedRepair(notification.getRepairId())) {
                                System.out.println("This repair is no longer in submitted state.");
                                NotificationController.maskAsRead(notification);
                                return;
                            }

                            if (RepairController.approveRepair(notification.getRepairId())) {
                                while (true){
                                    Integer employeeId = InputController.readInt("Employee ID> ");
                                    if (EmployeeController.existsEmployee(employeeId)){
                                        if (EmployeeController.availableForRepair(employeeId, notification.getRepairId())){
                                            NotificationController.assignRepairNotification(notification.getRepairId(), employeeId);
                                            NotificationController.maskAsRead(notification);
                                            System.out.println("Repair approved with success, and the employee was notified!");
                                            return;
                                        }else{
                                            System.out.println("Employee already rejected this repair");
                                        }
                                    }else {
                                        System.out.println("Invalid Employee ID");
                                    }
                                }



                            } else {
                                System.out.println("There was an error approving the repair");
                            }
                            return;

                        case "n", "no":

                            if (!RepairController.existsSubmittedRepair(notification.getRepairId())) {
                                System.out.println("This repair is no longer in submitted state.");
                                return;
                            }

                            Integer clientId = RepairController.getClientIdByRepair(notification.getRepairId());

                            if (clientId != null && RepairController.rejectRepair(notification.getRepairId())) {
                                NotificationController.rejectRepairNotification(clientId, notification.getRepairId());
                                NotificationController.maskAsRead(notification);
                                System.out.println("Repair rejected and archived with success!");
                            } else {
                                System.out.println("There was an error rejecting the repair");
                            }
                            return;

                        default:
                            System.out.println("Invalid Option.");
                            break;
                    }
                }

            case "assignEmployee":
                while (true) {
                    System.out.println("\nDo you want to accept the repair?");
                    System.out.println("(Yes/No)");

                    switch (InputController.readString("> ").trim().toLowerCase()) {
                        case "y", "yes":
                            if (RepairController.acceptRepair(notification.getRepairId(),notification.getAccountId())){
                            System.out.println("You were assigned to the repair!");
                            NotificationController.maskAsRead(notification);
                            return;
                            }
                        System.out.println("There was a problem accepting the repair");

                        case "n", "no":
                            NotificationController.dennyRepair(notification.getAccountId(), notification.getRepairId());
                            System.out.println("You declined being assigned to the repair!");
                            NotificationController.maskAsRead(notification);
                            return;

                        default:
                            System.out.println("Invalid Option.");
                            break;
                    }
                }
            case "reassignEmployee":

                while (true){
                    System.out.println("Assign a new Employee:");
                    Integer employeeId = InputController.readInt("Employee ID> ");
                    if (EmployeeController.existsEmployee(employeeId)){
                        if (EmployeeController.availableForRepair(employeeId, notification.getRepairId())){
                            NotificationController.assignRepairNotification(notification.getRepairId(), employeeId);
                            NotificationController.maskAsRead(notification);
                            System.out.println("Employee was notified!");
                            return;
                        }else{
                            System.out.println("Employee already rejected this repair");
                        }
                    }else {
                        System.out.println("Invalid Employee ID");
                    }
                }


                
            case "repairFinished":
                System.out.println(notification.toString());
                NotificationController.maskAsRead(notification);
                InputController.readString("--Press Enter to Leave--");
                return;

            default:
                System.out.println("There was a problem getting the notification");
                return;
        }
    }
}
