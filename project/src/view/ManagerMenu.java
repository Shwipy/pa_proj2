package view;
import controller.*;
import model.*;

import java.util.ArrayList;

/**
 * Displayed to console multiple menus for the manager to interact with the app
 */
public class ManagerMenu {

    /**
     * Enables the creation of a new Manager object,
     * Attempts to insert the created Manager into the Database
     */
    public static void showCreationMenu() {

        System.out.println("Creating manager: ");

        Manager manager = new Manager(
                null,
                InputController.readString("Name> "),
                InputController.readString("Username> "),
                true,
                true,
                InputController.readString("Password> "),
                InputController.readEmail("Email> "),
                "manager"
        );

        if (ManagerController.addManager(manager)) {
            System.out.println("Manager added with success!");
        } else {
            System.out.println("Error creating manager!");
        }
    }

    /**
     * Displays the menu for a manager account
     * @param manager that is accessing the menu
     */
    public static void showManagerMainMenu(Manager manager) {
        System.out.println("\nWelcome [" + manager.getName() + "]!");

        while (true) {
            System.out.println("\nManager Menu:");
            System.out.println("1- Manage Accounts");
            System.out.println("2- Manage Repairs");
            System.out.println("3- Manage Parts");
            System.out.println("4- Notifications");
            System.out.println("5- Edit User Settings");
            System.out.println("6- Logout");

            switch (InputController.readInt("> ")) {
                case 1:
                    showManageAccounts(manager);
                    break;

                case 2:
                	showManageRepairs();
                    break;

                case 3:
                    showManagePartsMenu();
                    break;

                case 4:
                    showNotificationMenu(manager);
                    break;

                case 5:
                    showManagerSettings(manager);
                    break;

                case 6:
                    System.out.println("\nLogging off..");
                    System.out.println("Goodbye [" + manager.getName() + "]!");
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void showManageAccounts(Manager manager) {

        Integer j = 0;
        Boolean asc = true;

        while (true) {
            System.out.println("\nManage Account Menu:");
            ArrayList<Manager> users = ManagerController.listUsers(j,asc);

            for (Manager user : users) {
                System.out.println(user.toString());
            }

            System.out.println("['<'- Back      '>'- Next Page]");
            System.out.println("1- Invert Order");
            System.out.println("2- View Account");
            System.out.println("3- Create Manager Account");
            System.out.println("4- Filter");
            System.out.println("5- Ban/Unban Account");
            System.out.println("6- Edit Account");
            System.out.println("7- Exit");

            Integer id;
            String type;
            switch (InputController.readString("> ")) {
                case ">":
                    j += 1;
                    users = ManagerController.listUsers(j,asc);

                    if (users.size() == 0) {
                        j -= 1;
                        users = ManagerController.listUsers(j,asc);
                        System.out.println("No more pages");
                    }
                    break;

                case "<":
                    if (j - 1 < 0) {
                        System.out.println("This is the last page");
                    } else {
                        j -= 1;
                        users = ManagerController.listUsers(j,asc);
                    }
                    break;

                case "1":
                    asc = !asc;
                    users = ManagerController.listUsers(j,asc);
                    break;

                case "2":
                    id = InputController.readInt("View Account> ");
                    type = SessionController.getAccountType(id);
                    switch (type){
                        case "client":
                            System.out.println(ClientController.getClient(id).toString());
                            InputController.readString("--Press Enter to Leave--");
                            break;

                        case "employee":
                            System.out.println(EmployeeController.getEmployee(id).toFormatedString());
                            InputController.readString("--Press Enter to Leave--");
                            break;

                        case "manager":
                            System.out.println(ManagerController.getManager(id).toString());
                            InputController.readString("--Press Enter to Leave--");
                            break;

                        case null:
                        default:
                            System.out.println("User id not found!");
                            break;
                    }
                    break;

                case "3":
                    showCreationMenu();
                    break;

                case "4":
                    filterAccountMenu();
                    break;

                case "5":
                    System.out.println("1- Ban Account ");
                    System.out.println("2- Unban Account");
                    System.out.println("3- Exit");
                    switch (InputController.readInt(">")){
                        case 1:
                            if(ManagerController.banAccount(InputController.readInt("Account ID>"),"ban")){
                                System.out.println("Account banned with success!");
                            }else{
                                System.out.println("There was an error banning the account");
                            }
                            break;

                        case 2:
                            if(ManagerController.banAccount(InputController.readInt("Account ID>"),"unban")){
                                System.out.println("Account unbanned with success!");
                            }else{
                                System.out.println("There was an error unbanned the account");
                            }
                            break;

                        case 3:
                            return;
                        default:
                            System.out.println("Invalid Input");
                            break;
                    }
                    break;

                case "6":
                    id = InputController.readInt("Account ID> ");
                    type = SessionController.getAccountType(id);
                    switch (type){
                        case "client":
                            Client client = ClientController.getClient(id);
                            ClientMenu.showAccountEdit(client);
                            break;

                        case "employee":
                            Employee employee = EmployeeController.getEmployee(id);
                            EmployeeMenu.showAccountEdit(employee);
                            break;

                        case "manager":
                            Manager man = ManagerController.getManager(id);
                            ManagerMenu.showAccountEdit(man);
                            break;

                        case null:
                        default:
                            System.out.println("User id not found!");
                            break;
                    }
                    break;

                case "7":
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void filterAccountMenu() {

        while(true){

            System.out.println("Filter Menu");
            System.out.println("1- Filter by Name");
            System.out.println("2- Filter by Username");
            System.out.println("3- Filter by Type");
            System.out.println("4- Exit");

            String search;
            ArrayList<Manager> filteredUsers;
            switch (InputController.readInt("> ")){

                case 1:
                     search = InputController.readString("Name> ");
                    System.out.println(search);
                    filteredUsers = ManagerController.searchUsers(search,"name");

                    for (Manager user : filteredUsers) {
                        System.out.println(user.toString());
                    }

                    break;

                case 2:
                    search = InputController.readString("Username> ");
                    System.out.println(search);
                    filteredUsers = ManagerController.searchUsers(search,"username");

                    for (Manager user : filteredUsers) {
                        System.out.println(user.toString());
                    }
                    break;

                case 3:
                    search = InputController.readString("Type> ");
                    System.out.println(search);
                    filteredUsers = ManagerController.searchUsers(search,"type");

                    for (Manager user : filteredUsers) {
                        System.out.println(user.toString());
                    }
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }


    private static void showManageRepairs() {

        Integer j = 0;
        Boolean asc = true;
        String orderBy = "date";

        ArrayList<Repair> repairs = RepairController.listAllRepairs(j, asc, orderBy);

        while (true) {
            System.out.println("\nManage Repairs:");

            if (repairs != null && repairs.size() > 0) {
                for (Repair repair : repairs) {
                    System.out.println(repair.toFormatedString());
                }
            } else {
                System.out.println("No repairs found.");
            }

            System.out.println("['<'- Back      '>'- Next Page]");
            System.out.println("1- View Details");
            System.out.println("2- Order by Date");
            System.out.println("3- Order by Repair Number");
            System.out.println("4- Invert Order");
            System.out.println("5- List Unfinished Repairs");
            System.out.println("6- Filter");
            System.out.println("7- Return");
            
            switch (InputController.readString("> ")) {

                case ">":
                    j += 1;
                    repairs = RepairController.listAllRepairs(j, asc, orderBy);

                    if (repairs.size() == 0) {
                        j -= 1;
                        repairs = RepairController.listAllRepairs(j, asc, orderBy);
                        System.out.println("No more pages");
                    }
                    break;

                case "<":
                    if (j - 1 < 0) {
                        System.out.println("This is the first page");
                    } else {
                        j -= 1;
                        repairs = RepairController.listAllRepairs(j, asc, orderBy);
                    }
                    break;

                case "1":
                    viewRepairDetailsMenu();
                    repairs = RepairController.listAllRepairs(j, asc, orderBy);
                    break;

                case "2":
                    orderBy = "date";
                    j = 0;
                    repairs = RepairController.listAllRepairs(j, asc, orderBy);
                    break;

                case "3":
                    orderBy = "number";
                    j = 0;
                    repairs = RepairController.listAllRepairs(j, asc, orderBy);
                    break;

                case "4":
                    asc = !asc;
                    j = 0;
                    repairs = RepairController.listAllRepairs(j, asc, orderBy);
                    break;

                case "5":
                    showUnfinishedRepairsMenu();
                    repairs = RepairController.listAllRepairs(j, asc, orderBy);
                    break;

                case "6":
                    filterMenu();
                    repairs = RepairController.listAllRepairs(j, asc, orderBy);
                    break;

                case "7":
                    return;
            }
        }
    }

    private static void showNotificationMenu(Manager manager){
        while (true){
            System.out.println("\nNotifications: ");

            System.out.println("1- New Notifications");
            System.out.println("2- Old Notifications");
            System.out.println("3- Exit");

            switch (InputController.readInt("> ")) {

                case 1:
                    NotificationMenu.showNotifications(manager.getId());
                    break;
                case 2:
                    System.out.println("WIP");
                    //NotificationMenu.showNotifications(true);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }


    private static void viewRepairDetailsMenu() {
        int repairId = InputController.readInt("Repair ID> ");

        Repair repair = RepairController.getRepair(repairId);

        if (repair != null) {
            System.out.println("\nRepair Details:");
            System.out.println(repair.toFormatedString());
            InputController.readString("--Press Enter to Leave--");
        } else {
            System.out.println("Repair not found.");
        }
    }

    private static void showUnfinishedRepairsMenu() {

        Integer j = 0;
        Boolean asc = true;

        ArrayList<Repair> repairs = RepairController.listUnfinishedRepairs(j, asc);

        while (true) {
            System.out.println("\nUnfinished Repairs:");

            if (repairs != null && repairs.size() > 0) {
                for (Repair repair : repairs) {
                    System.out.println(repair.toFormatedString());
                }
            } else {
                System.out.println("No unfinished repairs found.");
            }

            System.out.println("['<'- Back      '>'- Next Page]");
            System.out.println("1- Invert Order");
            System.out.println("2- Return");

            switch (InputController.readString("> ")) {

                case ">":
                    j += 1;
                    repairs = RepairController.listUnfinishedRepairs(j, asc);

                    if (repairs.size() == 0) {
                        j -= 1;
                        repairs = RepairController.listUnfinishedRepairs(j, asc);
                        System.out.println("No more pages");
                    }
                    break;

                case "<":
                    if (j - 1 < 0) {
                        System.out.println("This is the first page");
                    } else {
                        j -= 1;
                        repairs = RepairController.listUnfinishedRepairs(j, asc);
                    }
                    break;

                case "1":
                    asc = !asc;
                    j = 0;
                    repairs = RepairController.listUnfinishedRepairs(j, asc);
                    break;

                case "2":
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void filterMenu() {

        while (true) {
            System.out.println("\nFilter Menu:");
            System.out.println("1- Filter Repairs");
            System.out.println("2- Filter Equipments");
            System.out.println("3- List Equipments");
            System.out.println("4- Exit");

            switch (InputController.readInt("> ")) {

            case 1:
                filterRepairMenu();
                break;

            case 2:
                filterEquipmentMenu();
                break;

            case 3:
                showAllEquipmentsMenu();
                break;

            case 4:
                return;
                
            default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private static void filterRepairMenu() {

        while (true) {

            System.out.println("Filter Repair Menu");
            System.out.println("1- Filter by Repair Number");
            System.out.println("2- Filter by State");
            System.out.println("3- Filter by Client");
            System.out.println("4- Exit");

            String search;
            ArrayList<Repair> filteredRepairs;

            switch (InputController.readInt("> ")) {

                case 1:
                    search = InputController.readString("Repair Number> ");
                    filteredRepairs = RepairController.searchRepairs(search, "number");

                    if (filteredRepairs != null && filteredRepairs.size() > 0) {
                        for (Repair repair : filteredRepairs) {
                            System.out.println(repair.toFormatedString());
                        }
                    } else {
                        System.out.println("No repairs found.");
                    }
                    break;

                case 2:
                    search = InputController.readString("State> ");
                    filteredRepairs = RepairController.searchRepairs(search, "state");

                    if (filteredRepairs != null && filteredRepairs.size() > 0) {
                        for (Repair repair : filteredRepairs) {
                            System.out.println(repair.toFormatedString());
                        }
                    } else {
                        System.out.println("No repairs found.");
                    }
                    break;

                case 3:
                    search = InputController.readString("Client Name> ");
                    filteredRepairs = RepairController.searchRepairs(search, "client");

                    if (filteredRepairs != null && filteredRepairs.size() > 0) {
                        for (Repair repair : filteredRepairs) {
                            System.out.println(repair.toFormatedString());
                        }
                    } else {
                        System.out.println("No repairs found.");
                    }
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private static void filterEquipmentMenu() {

        while (true) {

            System.out.println("Filter Equipment Menu");
            System.out.println("1- Filter by Brand");
            System.out.println("2- Filter by Code");
            System.out.println("3- Exit");

            String search;
            ArrayList<Equipment> filteredEquipments;

            switch (InputController.readInt("> ")) {

                case 1:
                    search = InputController.readString("Brand> ");
                    filteredEquipments = EquipmentController.searchEquipments(search, "brand");

                    if (filteredEquipments != null && filteredEquipments.size() > 0) {
                        for (Equipment equipment : filteredEquipments) {
                            System.out.println(equipment.toString());
                        }
                    } else {
                        System.out.println("No equipments found.");
                    }
                    break;

                case 2:
                    search = InputController.readString("Code> ");
                    filteredEquipments = EquipmentController.searchEquipments(search, "sku");

                    if (filteredEquipments != null && filteredEquipments.size() > 0) {
                        for (Equipment equipment : filteredEquipments) {
                            System.out.println(equipment.toString());
                        }
                    } else {
                        System.out.println("No equipments found.");
                    }
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private static void showAllEquipmentsMenu() {

        Integer j = 0;

        ArrayList<Equipment> equipments = EquipmentController.listAllEquipments(j);

        while (true) {
            System.out.println("\nAll Equipments:");

            if (equipments != null && equipments.size() > 0) {
                for (Equipment equipment : equipments) {
                    System.out.println(equipment.toString());
                }
            } else {
                System.out.println("No equipments found.");
            }

            System.out.println("['<'- Back      '>'- Next Page]");
            System.out.println("1- Return");

            switch (InputController.readString("> ")) {

                case ">":
                    j += 1;
                    equipments = EquipmentController.listAllEquipments(j);

                    if (equipments.size() == 0) {
                        j -= 1;
                        equipments = EquipmentController.listAllEquipments(j);
                        System.out.println("No more pages");
                    }
                    break;

                case "<":
                    if (j - 1 < 0) {
                        System.out.println("This is the first page");
                    } else {
                        j -= 1;
                        equipments = EquipmentController.listAllEquipments(j);
                    }
                    break;

                case "1":
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void showManagePartsMenu() {
        while (true) {
            PartController.listParts();

            System.out.println("\nManage Parts Menu:");
            System.out.println("1- Add Part");
            System.out.println("2- Update Part Amount");
            System.out.println("3- Return");

            switch (InputController.readInt("> ")) {
                case 1:
                    addPartMenu();
                    break;

                case 2:
                    updatePartAmountMenu();
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void addPartMenu() {
        System.out.println("\nAdd Part:");

        Part part = new Part(
                -1,
                null,
                InputController.readString("Code> "),
                InputController.readString("Designation> "),
                InputController.readString("Manufacturer> "),
                InputController.readInt("Amount> ")
        );

        if (PartController.addPart(part)) {
            System.out.println("Part added successfully.");
        } else {
            System.out.println("Error adding part.");
        }
    }

    private static void updatePartAmountMenu() {
        System.out.println("\nUpdate Part Amount:");

        int partId = InputController.readInt("Part ID> ");

        if (!PartController.existsPart(partId)) {
            System.out.println("Invalid part.");
            return;
        }

        int newAmount = InputController.readInt("New Amount> ");

        if (PartController.updatePartAmount(partId, newAmount)) {
            System.out.println("Part amount updated successfully.");
        } else {
            System.out.println("Error updating part amount.");
        }
    }

    private static void showManagerSettings(Manager manager) {

        while (true) {
            System.out.println("\nManager Settings Menu:");
            System.out.println("1- View Account Data");
            System.out.println("2- Edit Account Settings");
            System.out.println("3- Remove Account");
            System.out.println("4- Exit");

            switch (InputController.readInt("> ")) {
                case 1:
                    System.out.println(manager.toFormatedString());
                    break;

                case 2:
                    showAccountEdit(manager);
                    break;

                case 3:
                    // talvez não seja preciso para o manager
                    showAccountRemove(manager);
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    /**
     * Displays an interface for a manager to edit one of its attributes;
     * @param manager object Manager that contains all the data of the manager that is accessing the menu
     */
    public static void showAccountEdit(Manager manager) {

        while (true) {
            System.out.println("\nWhat would you like to change?");
            System.out.println("1- Name");
            System.out.println("2- Username");
            System.out.println("3- Password");
            System.out.println("4- Email");
            System.out.println("5- Exit");

            switch (InputController.readInt("> ")) {
                case 1:
                    manager.setName(InputController.readString("New Name> "));
                    if (ManagerController.updateManager(manager)) {
                        System.out.println("Your Name was Updated with success!");
                        return;
                    } else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 2:
                    manager.setUsername(InputController.readString("New Username> "));
                    if (ManagerController.updateManager(manager)) {
                        System.out.println("Your Username was Updated with success!");
                        return;
                    } else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 3:
                    manager.setPassword(InputController.readString("New Password> "));
                    if (ManagerController.updateManager(manager)) {
                        System.out.println("Your Password was Updated with success!");
                        return;
                    } else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 4:
                    manager.setEmail(InputController.readEmail("New Email> "));
                    if (ManagerController.updateManager(manager)) {
                        System.out.println("Your Email was Updated with success!");
                        return;
                    } else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void showAccountRemove(Manager manager) {

        while (true) {
            System.out.println("\nDo you really want to delete the account?");
            System.out.println("(Yes/No)");

            switch (InputController.readString("> ").trim().toLowerCase()) {
                case "y", "yes":
                    // depois o manager ao dizer que sim passa o compo active para false e apaga os dados do cliente.
                    System.out.println("A manager was notified of your request.");
                    System.out.println("Apagou!");
                    System.exit(0);

                case "n", "no":
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }
}
