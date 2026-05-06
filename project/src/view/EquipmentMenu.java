package view;

import controller.EquipmentController;
import controller.InputController;
import model.Equipment;
/**
 * Displayed to console a menu for users to add an equipment
 */
public class EquipmentMenu {

    /**
     * Displayed to console a menu for users to add an equipment;
     * Apparently this functions was created but never used?
     */
    public static void showCreationMenu(){
        System.out.println("Creating equipment:");

        Equipment equipment = new Equipment(
                -1,
                InputController.readInt("Client ID> "),
                InputController.readString("Brand> "),
                InputController.readString("Model> "),
                InputController.readString("SKU> "),
                InputController.readString("Batch> "),
                InputController.readLong("Manufacture Date> "),
                InputController.readLong("Repair Date> "),
                System.currentTimeMillis()/1000
        );

        if (EquipmentController.addEquipment(equipment)) {
            System.out.println("Equipment added with success!");
        } else {
            System.out.println("Error creating equipment!");
        }
    }
}
