import controller.OutputController;
import controller.SessionController;
import view.LoginGUI;


/**
 * Starting point of the application
 */
public class Main {

    /**
     * Starts the application
     * Checks for users, if no users are in the Database the user gets redirected to the manager creation menu
     */
    public static void main(String[] args) {
        //SessionController.addLogExecution();
        Long start = System.currentTimeMillis();

        Boolean hasUsers = SessionController.hasUsers();

        if (hasUsers == null) {
            System.out.println("Erro ao verificar utilizadores.");
            return;
        }

        if (!hasUsers) {
            System.out.println("There are no available users on the app.");
            //ManagerMenu.showCreationMenu();
        }

        //MainMenu.show();

        //Corre porca!!!!
        LoginGUI menu = new LoginGUI();
        menu.init();
        menu.setVisible(true);


        OutputController.appTimer(start);
        System.out.println("Thank you for using our app!!");
    }
}
