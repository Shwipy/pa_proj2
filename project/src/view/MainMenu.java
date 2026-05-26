//package view;
//
//import controller.*;
//import model.Client;
//import model.DataBase;
//import model.Employee;
//
///**
// * Displayed to console the menu that is accessed by all accounts before authentication
// */
//public class MainMenu {
//    /**
//     * Shows users a starting menu that will redirect them to account creation or authentication
//     * When creating an account users must select if the created account is a client or employee account
//     */
//    public static void show(){
//        while (true){
//            System.out.println("\nMenu:");
//            System.out.println("1 - Create");
//            System.out.println("2 - Login");
//
//            System.out.println("3 - Exit");
//
//            switch (InputController.readInt("> ")){
//                case 1:
//                    System.out.println("\nCreating account:");
//                    System.out.println("1 - Client");
//                    System.out.println("2 - Employee");
//
//                    System.out.println("3 - Return");
//
//                    switch (InputController.readInt("> ")){
//
//                        case 1:
//                            ClientMenu.showCreationMenu();
//                            break;
//
//                        case 2:
//                            EmployeeMenu.showCreationMenu();
//                            break;
//
//                        case 3:
//                            break;
//
//                        default:
//                            System.out.println("Invalid Option.");
//                            break;
//                    }
//                    break;
//
//                case 2:
//                    System.out.println("Login:");
//
//                    Integer userId = SessionController.login(
//                            InputController.readString("Username> "),
//                            InputController.readString("Password> ")
//                    );
//
//                    if (userId==null){
//                        break;
//                    }
//
//                    switch (SessionController.getAccountType(userId)){
//
//                        case "manager":
//                            ManagerMenu.showManagerMainMenu(ManagerController.getManager(userId));
//                            break;
//
//                        case "client":
//                            ClientMenu.showClientMainMenu(ClientController.getClient(userId));
//                            break;
//
//                        case "employee":
//                            EmployeeMenu.showEmployeeMenu(EmployeeController.getEmployee(userId));
//                            break;
//
//                        case null:
//                        default:
//                            System.out.println("Error During Login");
//                            break;
//
//                    }
//
//                    break;
//
//                case 3:
//                    return;
//
//                default:
//                    System.out.println("Invalid Option.");
//                    break;
//            }
//        }
//    }
//}
