package view;

import controller.*;
import model.Client;
import model.DataBase;
import model.Equipment;
import model.Repair;
import java.util.ArrayList;

/**
 * Displayed to console multiple menus for the clients to interact with the app
 */
public class ClientMenu {

    /**
     * Enables the creation of a new Client object,
     * Attempts to insert the created Client into the Database
     */
    public static void  showCreationMenu(){
        System.out.println("Creating client account:");

        Client client = new Client(
                -1,
                InputController.readString("Name> "),
                InputController.readString("Username> "),
                InputController.readString("Password> "),
                true,
                false,
                InputController.readEmail("Email> "),
                "client",
                InputController.readNif("NIF> "),
                InputController.readPhone("Phone> "),
                InputController.readString("Residence> "),
                InputController.readString("Business Sector> "),
                ""
        );

        if(ClientController.addClient(client)){
            System.out.println("Your account was created and is waiting approval.");
        }else{
            System.out.println("Something went wrong during the creation of the account.");
        }
    }

    /**
     * Displays the main menu of a client type account;
     * Alows the user to call the functions to show the sub-menus for the client type account
     * @param client object Client that contains all the data of the client that is accessing the menu
     */
    public static void  showClientMainMenu(Client client) {
        System.out.println("\nWelcome [" + client.getName() + "]!");

        while (true) {
            System.out.println("\nClient Menu:");
            System.out.println("1- Equipments");
            System.out.println("2- Repairs");
            System.out.println("3- Notifications");
            System.out.println("4- Edit User Settings");
            System.out.println("5- Logout");

            switch (InputController.readInt("> ")) {
                case 1:
                    showClientEquipmentMenu(client);
                    break;

                case 2:
                    showClientRepairMenu(client);
                    break;

                case 3:
                    showNotificationMenu(client);
                    break;

                case 4:
                    showUserSettings(client);
                    break;

                case 5:
                    System.out.println("\nLogging off..");
                    System.out.println("Goodbye [" + client.getName() + "]!");
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void showClientEquipmentMenu(Client client) {

        while (true) {
            System.out.println("\nEquipment Menu:");
            System.out.println("1- Add Equipment");
            System.out.println("2- List Equipment");
            System.out.println("3- Search Equipment");

            System.out.println("4- Exit");

            switch (InputController.readInt("> ")) {
            	case 1:
                String sku = EquipmentController.generateSKU();

                System.out.println("Equipment created with SKU: " + sku);

                Equipment equipment = new Equipment(
                        -1,
                        client.getId(),
                        InputController.readString("Brand> "),
                        InputController.readString("Model> "),
                        sku,
                        InputController.readString("Batch> "),
                        InputController.readDate(),
                        System.currentTimeMillis()/1000,
                        null
                );

                if (EquipmentController.addEquipment(equipment)) {
                    System.out.println("Equipment added with success!");
                } else {
                    System.out.println("Error creating equipment!");
                }
                break;

            	case 2:
            	    showAllEquipmentsMenu(client);
            	    break;

            	case 3:
            	    searchEquipmentsMenu(client);
            	    break;
            	    
                case 4:
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void showAllEquipmentsMenu(Client client) {

        Integer j = 0;
        Boolean asc = true;
        String orderBy = "brand";

        java.util.ArrayList<Equipment> equipments = EquipmentController.listEquipmentsByClient(j, asc, orderBy, client.getId());

        while (true) {
            System.out.println("\nMy Equipments:");

            if (equipments != null && equipments.size() > 0) {

                for (Equipment equipment : equipments) {
                    System.out.println(equipment.toString());
                }

            } else {
                System.out.println("No equipments found.");
            }

            System.out.println("['<'- Back      '>'- Next Page]");
            System.out.println("1- Order by Brand");
            System.out.println("2- Order by SKU");
            System.out.println("3- Invert Order");
            System.out.println("4- Return");

            switch (InputController.readString("> ")) {

                case ">":
                    j += 1;
                    equipments = EquipmentController.listEquipmentsByClient(j, asc, orderBy, client.getId());

                    if (equipments.size() == 0) {
                        j -= 1;
                        equipments = EquipmentController.listEquipmentsByClient(j, asc, orderBy, client.getId());
                        System.out.println("No more pages");
                    }
                    break;

                case "<":
                    if (j - 1 < 0) {
                        System.out.println("This is the first page");
                    } else {
                        j -= 1;
                        equipments = EquipmentController.listEquipmentsByClient(j, asc, orderBy, client.getId());
                    }
                    break;

                case "1":
                    orderBy = "brand";
                    j = 0;
                    equipments = EquipmentController.listEquipmentsByClient(j, asc, orderBy, client.getId());
                    break;

                case "2":
                    orderBy = "sku";
                    j = 0;
                    equipments = EquipmentController.listEquipmentsByClient(j, asc, orderBy, client.getId());
                    break;

                case "3":
                    asc = !asc;
                    j = 0;
                    equipments = EquipmentController.listEquipmentsByClient(j, asc, orderBy, client.getId());
                    break;

                case "4":
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void searchEquipmentsMenu(Client client) {

        while (true) {

            System.out.println("\nSearch Equipment Menu:");
            System.out.println("1- Search by Brand");
            System.out.println("2- Search by Code");
            System.out.println("3- Exit");

            String search;
            java.util.ArrayList<Equipment> filteredEquipments;

            switch (InputController.readInt("> ")) {

                case 1:
                    search = InputController.readString("Brand> ");
                    filteredEquipments = EquipmentController.searchEquipmentsByClient(search, "brand", client.getId());

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
                    filteredEquipments = EquipmentController.searchEquipmentsByClient(search, "sku", client.getId());

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

    private static void  showClientRepairMenu(Client client) {

        while (true) {
            System.out.println("\nRepair Menu:");
            System.out.println("1- Add Repair");
            System.out.println("2- List Repairs");
            System.out.println("3- Search Repairs");

            System.out.println("4- Exit");

            switch (InputController.readInt("> ")) {
            case 1:
                System.out.println("Creating repair request:");

                int equipmentId = InputController.readInt("Equipment ID> ");

                if (!EquipmentController.existsEquipmentForClient(equipmentId, client.getId())) {
                    System.out.println("Invalid equipment. This equipment does not belong to you.");
                    break;
                }

                if (RepairController.equipmentHasActiveRepair(equipmentId)) {
                    System.out.println("This equipment already has an active repair.");
                    break;
                }

                String repairNumber = RepairController.generateRepairNumber();

                System.out.println("Generated repair number: " + repairNumber);

                Repair repair = new Repair(
                        -1,
                        null,
                        equipmentId,
                        repairNumber,
                        System.currentTimeMillis()/1000,
                        null,
                        null,
                        null,
                        "submitted",
                        InputController.readString("Notes> ")
                );

                if (RepairController.addRepair(repair)) {
                    System.out.println("Repair request submitted successfully!");
                } else {
                    System.out.println("Error submitting repair request!");
                }
                break;

            	case 2:
                showAllClientRepairsMenu(client);
                break;

            	case 3:
            	    searchRepairsMenu(client);
            	    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void showAllClientRepairsMenu(Client client) {

        Integer j = 0;
        Boolean asc = true;
        String orderBy = "date";

        ArrayList<Repair> repairs = RepairController.listRepairsByClient(j, asc, orderBy, client.getId());

        while (true) {
            System.out.println("\nMy Repairs:");

            if (repairs != null && repairs.size() > 0) {
                for (Repair repair : repairs) {
                    System.out.println(repair.toFormatedString());
                }
            } else {
                System.out.println("No repairs found.");
            }

            System.out.println("['<'- Back      '>'- Next Page]");
            System.out.println("1- Order by Date");
            System.out.println("2- Order by Repair Number");
            System.out.println("3- Invert Order");
            System.out.println("4- Return");

            switch (InputController.readString("> ")) {

                case ">":
                    j += 1;
                    repairs = RepairController.listRepairsByClient(j, asc, orderBy, client.getId());

                    if (repairs.size() == 0) {
                        j -= 1;
                        repairs = RepairController.listRepairsByClient(j, asc, orderBy, client.getId());
                        System.out.println("No more pages");
                    }
                    break;

                case "<":
                    if (j - 1 < 0) {
                        System.out.println("This is the first page");
                    } else {
                        j -= 1;
                        repairs = RepairController.listRepairsByClient(j, asc, orderBy, client.getId());
                    }
                    break;

                case "1":
                    orderBy = "date";
                    j = 0;
                    repairs = RepairController.listRepairsByClient(j, asc, orderBy, client.getId());
                    break;

                case "2":
                    orderBy = "number";
                    j = 0;
                    repairs = RepairController.listRepairsByClient(j, asc, orderBy, client.getId());
                    break;

                case "3":
                    asc = !asc;
                    j = 0;
                    repairs = RepairController.listRepairsByClient(j, asc, orderBy, client.getId());
                    break;

                case "4":
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void searchRepairsMenu(Client client) {

        while (true) {

            System.out.println("\nSearch Repair Menu:");
            System.out.println("1- Search by Repair Number");
            System.out.println("2- Search by State");
            System.out.println("3- Exit");

            String search;
            ArrayList<Repair> filteredRepairs;

            switch (InputController.readInt("> ")) {

                case 1:
                    search = InputController.readString("Repair Number> ");
                    filteredRepairs = RepairController.searchRepairsByClient(search, "number", client.getId());

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
                    filteredRepairs = RepairController.searchRepairsByClient(search, "state", client.getId());

                    if (filteredRepairs != null && filteredRepairs.size() > 0) {
                        for (Repair repair : filteredRepairs) {
                            System.out.println(repair.toFormatedString());
                        }
                    } else {
                        System.out.println("No repairs found.");
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

    private static void  showNotificationMenu(Client client) {

        while (true) {
            System.out.println("\nNotifications :");
            System.out.println("1- New Notifications");
            System.out.println("2- Old Notifications");

            System.out.println("3- Exit");

            switch (InputController.readInt("> ")) {
                case 1:
                    NotificationMenu.showNotifications(client.getId());
                    break;

                case 2:
                    System.out.println("WIP");
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void  showUserSettings(Client client) {

        while (true) {
            System.out.println("\nUser Settings Menu:");
            System.out.println("1- View Account Data");
            System.out.println("2- Edit Account Settings");
            System.out.println("3- Remove Account");

            System.out.println("4- Exit");

            switch (InputController.readInt("> ")) {
                case 1:
                    System.out.println(client.toString());
                    break;

                case 2:
                    showAccountEdit(client);
                    break;

                case 3:
                    showAccountRemove(client);
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
     * Displays an interface for a client to edit one of its attributes;
     * @param client object Client that contains all the data of the client that is accessing the menu
     */
    public static void  showAccountEdit(Client client) {

        while (true) {
            System.out.println("\nWhat would you like to change?");
            //Account
            System.out.println("1- Name");
            System.out.println("2- Username");
            System.out.println("3- Password");
            System.out.println("4- Email");
            //Client
            System.out.println("5- NIF");
            System.out.println("6- Phone Number");
            System.out.println("7- Residence");
            System.out.println("8- Business Sector");

            System.out.println("9- Exit");


            switch (InputController.readInt("> ")) {
                case 1:
                    client.setName(InputController.readString("New Name> "));
                    if(ClientController.updateClient(client)){
                        System.out.println("Your Name was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 2:
                    client.setUsername(InputController.readString("New Username> "));
                    if(ClientController.updateClient(client)){
                        System.out.println("Your Username was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 3:
                    client.setPassword(InputController.readString("New Password> "));
                    if(ClientController.updateClient(client)){
                        System.out.println("Your Password was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 4:
                    client.setEmail(InputController.readEmail("New Email> "));
                    if(ClientController.updateClient(client)){
                        System.out.println("Your Email was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 5:
                    client.setNif(InputController.readNif("New NIF> "));
                    if(ClientController.updateClient(client)){
                        System.out.println("Your NIF was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 6:
                    client.setPhoneNumber(InputController.readPhone("New Phone Number> "));
                    if(ClientController.updateClient(client)){
                        System.out.println("Your Phone Number was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 7:
                    client.setResidence(InputController.readString("New Residence> "));
                    if(ClientController.updateClient(client)){
                        System.out.println("Your Residence was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 8:
                    client.setBusinessSector(InputController.readString("New Business Sector> "));
                    if(ClientController.updateClient(client)){
                        System.out.println("Your Business Sector was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 9:
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void  showAccountRemove(Client client) {

        while (true) {
            System.out.println("\nDo you really want to delete the account?");
            System.out.println("(Yes/No)");

            switch (InputController.readString("> ").trim().toLowerCase()) {
                case "y","yes":
                    System.out.println("A manager was notified of your request.");
                    NotificationController.deleteAccountNotification(client.getName(),client.getId());
                    System.exit(0);

                case "n","no":
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

}
