package view;
import controller.*;
import model.Client;
import model.Employee;
import model.Test;
import model.Repair;
import java.util.ArrayList;

/**
 * Displayed to console multiple menus for the employees to interact with the app
 */
public class EmployeeMenu {

    /**
     * Enables the creation of a new Employee object,
     * Attempts to insert the created Employee into the Database
     */
    public static void  showCreationMenu(){
        System.out.println("Creating employee account:");

        Employee employee = new Employee(
                -1,
                InputController.readString("Name> "),
                InputController.readString("Username> "),
                InputController.readString("Password> "),
                true,
                false,
                InputController.readEmail("Email> "),
                "employee",
                InputController.readNif("NIF> "),
                InputController.readPhone("Phone> "),
                InputController.readString("Residence> "),
                //adicionar confirmação que é entre 1 e 5 OU o gestor adiciona depois
                InputController.readString("Level of Expertise> "),
                System.currentTimeMillis()/1000
        );

        if(EmployeeController.addEmployee(employee)){
            System.out.println("Your account was created and is waiting approval.");
        }else{
            System.out.println("Something went wrong during the creation of the account.");
        }
    }

    /**
     * Displays the main menu of an employee type account;
     * Alows the user to call the functions to show the sub-menus for the employee type account
     * @param employee object Employee that contains all the data of the employee that is accessing the menu
     */
    public static void  showEmployeeMenu(Employee employee) {
        System.out.println("\nWelcome [" + employee.getName() + "]!");

        while (true) {
            System.out.println("\nEmployee Menu:");
            System.out.println("1- Repairs");
            System.out.println("2- Notifications");
            System.out.println("3- Edit User Settings");
            System.out.println("4- Logout");

            switch (InputController.readInt("> ")) {
                case 1:
                	showEmployeeRepairMenu(employee);
                    break;

                case 2:
                    showNotificationMenu(employee);
                    break;

                case 3:
                    showEmployeeSettings(employee);
                    break;

                case 4:
                    System.out.println("\nLogging off..");
                    System.out.println("Goodbye [" + employee.getName() + "]!");
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }

        }
    }

    private static void  showNotificationMenu(Employee employee) {

        while (true) {
            System.out.println("\nNotifications :");
            System.out.println("1- New Notifications");
            System.out.println("2- Old Notifications");

            System.out.println("3- Exit");

            switch (InputController.readInt("> ")) {
                case 1:
                    NotificationMenu.showNotifications(employee.getId());
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


    private static void showEmployeeRepairMenu(Employee employee) {
        while (true) {
            System.out.println("\nEmployee Repair Menu:");
            System.out.println("1- Manage My Repairs");
            System.out.println("2- Search My Repairs");
            System.out.println("3- Exit");

            switch (InputController.readInt("> ")) {
                case 1:
                    manageMyRepairs(employee);
                    break;

                case 2:
                    searchRepairsMenu(employee);
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void manageMyRepairs(Employee employee) {

        Integer j = 0;
        Boolean asc = true;
        String orderBy = "date";

        java.util.ArrayList<Repair> repairs = RepairController.listRepairsByEmployee(j, asc, orderBy, employee.getId());

        while (true) {
            System.out.println("\nMy Repairs:");

            if (repairs != null && repairs.size() > 0) {
                for (Repair repair : repairs) {
                    System.out.println(repair.toFormatedString());
                }
            } else {
                System.out.println("No repairs assigned to this employee.");
            }

            System.out.println("['<'- Back      '>'- Next Page]");
            System.out.println("1- Choose Repair");
            System.out.println("2- Order by Date");
            System.out.println("3- Order by Repair Number");
            System.out.println("4- Invert Order");
            System.out.println("5- Return");

            String option = InputController.readString("> ");

            switch (option) {

                case ">":
                    j += 1;
                    repairs = RepairController.listRepairsByEmployee(j, asc, orderBy, employee.getId());

                    if (repairs.size() == 0) {
                        j -= 1;
                        repairs = RepairController.listRepairsByEmployee(j, asc, orderBy, employee.getId());
                        System.out.println("No more pages");
                    }
                    break;

                case "<":
                    if (j - 1 < 0) {
                        System.out.println("This is the first page");
                    } else {
                        j -= 1;
                        repairs = RepairController.listRepairsByEmployee(j, asc, orderBy, employee.getId());
                    }
                    break;

                case "1":
                    int repairId = InputController.readInt("Repair ID> ");

                    if (!RepairController.existsAssignedRepairForEmployee(repairId, employee.getId())) {
                        System.out.println("This repair is not assigned to you.");
                        break;
                    }

                    String state = RepairController.getRepairState(repairId);

                    if (state == null) {
                        System.out.println("Error getting repair state.");
                        break;
                    }

                    if (state.equals("accepted")) {

                        System.out.println("\nRepair Action:");
                        System.out.println("1- Start Repair");
                        System.out.println("2- Cancel");

                        int action = InputController.readInt("> ");

                        switch (action) {
                            case 1:
                                if (RepairController.startRepair(repairId, employee.getId())) {
                                    System.out.println("Repair started successfully.");
                                } else {
                                    System.out.println("Could not start repair.");
                                }
                                break;

                            case 2:
                                break;

                            default:
                                System.out.println("Invalid Option.");
                                break;
                        }
                    }

                    else if (state.equals("in_progress")) {

                        System.out.println("\nRepair Action:");
                        System.out.println("1- Add Test");
                        System.out.println("2- List Tests");
                        System.out.println("3- Add Part");
                        System.out.println("4- List Parts Used");
                        System.out.println("5- Finish Repair");
                        System.out.println("6- Cancel");

                        int action = InputController.readInt("> ");

                        switch (action) {
                            case 1:
                                Test test = new Test(
                                        -1,
                                        employee.getId(),
                                        repairId,
                                        InputController.readString("Designation> "),
                                        InputController.readString("Description> "),
                                        System.currentTimeMillis()/1000
                                );

                                if (TestController.addTest(test)) {
                                    System.out.println("Test added successfully.");
                                } else {
                                    System.out.println("Could not add test.");
                                }
                                break;

                            case 2:
                                TestController.listTestsByRepair(repairId);
                                break;

                            case 3:
                                PartController.listParts();

                                int partId = InputController.readInt("Part ID> ");
                                int amountUsed = InputController.readInt("Amount Used> ");

                                if (PartRepairController.addPartToRepair(repairId, partId, amountUsed)) {
                                    System.out.println("Part added successfully.");
                                } else {
                                    System.out.println("Could not add part.");
                                }
                                break;

                            case 4:
                                PartRepairController.listPartsByRepair(repairId);
                                break;

                            case 5:
                                String notes = InputController.readString("Notes> ");
                                Double price = null;

                                try {
                                    price = Double.parseDouble(InputController.readString("Price> "));
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid price.");
                                    break;
                                }

                                if (RepairController.finishRepair(repairId, employee.getId(), notes, price)) {
                                    System.out.println("Repair finished successfully.");
                                } else {
                                    System.out.println("Could not finish repair.");
                                }
                                break;

                            case 6:
                                break;

                            default:
                                System.out.println("Invalid Option.");
                                break;
                        }
                    }

                    else if (state.equals("finished")) {

                        System.out.println("\nRepair Action:");
                        System.out.println("1- List Tests");
                        System.out.println("2- Cancel");

                        int action = InputController.readInt("> ");

                        switch (action) {
                            case 1:
                                TestController.listTestsByRepair(repairId);
                                break;

                            case 2:
                                break;

                            default:
                                System.out.println("Invalid Option.");
                                break;
                        }
                    }

                    else {
                        System.out.println("Invalid repair state.");
                    }

                    repairs = RepairController.listRepairsByEmployee(j, asc, orderBy, employee.getId());
                    break;

                case "2":
                    orderBy = "date";
                    j = 0;
                    repairs = RepairController.listRepairsByEmployee(j, asc, orderBy, employee.getId());
                    break;

                case "3":
                    orderBy = "number";
                    j = 0;
                    repairs = RepairController.listRepairsByEmployee(j, asc, orderBy, employee.getId());
                    break;

                case "4":
                    asc = !asc;
                    j = 0;
                    repairs = RepairController.listRepairsByEmployee(j, asc, orderBy, employee.getId());
                    break;

                case "5":
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }
    
    private static void searchRepairsMenu(Employee employee) {

        while (true) {

            System.out.println("\nSearch Repair Menu:");
            System.out.println("1- Search by Repair Number");
            System.out.println("2- Search by State");
            System.out.println("3- Exit");

            String search;
            java.util.ArrayList<Repair> filteredRepairs;

            switch (InputController.readInt("> ")) {

                case 1:
                    search = InputController.readString("Repair Number> ");
                    filteredRepairs = RepairController.searchRepairsByEmployee(search, "number", employee.getId());

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
                    filteredRepairs = RepairController.searchRepairsByEmployee(search, "state", employee.getId());

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
    
    private static void  showEmployeeSettings(Employee employee) {

        while (true) {
            System.out.println("\nEmployee Settings Menu:");
            System.out.println("1- View Account Data");
            System.out.println("2- Edit Account Settings");
            System.out.println("3- Remove Account");

            System.out.println("4- Exit");

            switch (InputController.readInt("> ")) {
                case 1:
                    System.out.println(employee.toFormatedString());
                    break;

                case 2:
                    showAccountEdit(employee);
                    break;

                case 3:
                    showAccountRemove(employee);
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
     * Displays an interface for an employee to edit one of its attributes;
     * @param employee object Employee that contains all the data of the employee that is accessing the menu
     */
    public static void  showAccountEdit(Employee employee) {

        while (true) {
            System.out.println("\nWhat would you like to change?");
            //Account
            System.out.println("1- Name");
            System.out.println("2- Username");
            System.out.println("3- Password");
            System.out.println("4- Email");
            //Employee
            System.out.println("5- NIF");
            System.out.println("6- Phone Number");
            System.out.println("7- Residence");

            System.out.println("8- Exit");


            switch (InputController.readInt("> ")) {
                case 1:
                    employee.setName(InputController.readString("New Name> "));
                    if(EmployeeController.updateEmployee(employee)){
                        System.out.println("Your Name was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 2:
                    employee.setUsername(InputController.readString("New Username> "));
                    if(EmployeeController.updateEmployee(employee)){
                        System.out.println("Your Username was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 3:
                    employee.setPassword(InputController.readString("New Password> "));
                    if(EmployeeController.updateEmployee(employee)){
                        System.out.println("Your Password was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 4:
                    employee.setEmail(InputController.readEmail("New Email> "));
                    if(EmployeeController.updateEmployee(employee)){
                        System.out.println("Your Email was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 5:
                    employee.setNif(InputController.readNif("New NIF> "));
                    if(EmployeeController.updateEmployee(employee)){
                        System.out.println("Your NIF was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 6:
                    employee.setPhoneNumber(InputController.readPhone("New Phone Number> "));
                    if(EmployeeController.updateEmployee(employee)){
                        System.out.println("Your Phone Number was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 7:
                    employee.setResidence(InputController.readString("New Residence> "));
                    if(EmployeeController.updateEmployee(employee)){
                        System.out.println("Your Residence was Updated with success!");
                        return;
                    }else {
                        System.out.println("The was a problem Updating the account.");
                    }
                    break;

                case 8:
                    return;

                default:
                    System.out.println("Invalid Option.");
                    break;
            }
        }
    }

    private static void  showAccountRemove(Employee employee) {

        while (true) {
            System.out.println("\nDo you really want to delete the account?");
            System.out.println("(Yes/No)");

            switch (InputController.readString("> ").trim().toLowerCase()) {
                case "y","yes":
                    System.out.println("A manager was notified of your request.");
                    NotificationController.deleteAccountNotification(employee.getName(),employee.getId());
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
