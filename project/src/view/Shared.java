package view;

import controller.EmployeeController;
import controller.NotificationController;
import model.Employee;
import model.Notification;

import javax.swing.*;
import java.util.ArrayList;

public class Shared {

    public static Boolean showOkCancelDialog(JFrame frame, String message, String title){
        int dialogResult = JOptionPane.showConfirmDialog(
                frame,
                message,
                title,
                JOptionPane.OK_CANCEL_OPTION
        );

        return dialogResult == 0;
    }

    public static Boolean showYesNoDialog(JFrame frame, String message, String title){
        int dialogResult = JOptionPane.showConfirmDialog(
                frame,
                message,
                title,
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        return dialogResult == 0;
    }

    public static void showErrorDialog(JFrame frame, String message, String title){
        JOptionPane.showMessageDialog(
                frame,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );

    }

    public static void showInformationDialog(JFrame frame, String message, String title){
        JOptionPane.showMessageDialog(
                frame,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //work in progress
    public static Integer showSelectEmployeeDialog(JFrame frame,String message, String title, Integer employeeId, Integer repairId){
        JLabel myLabel1 = new JLabel("Select the Employee for this Repair:");
        JComboBox employeeCombobox = new JComboBox();
        //ArrayList<Employee> availableEmployeeList = EmployeeController.availableForRepair(employeeId,repairId);
        DefaultComboBoxModel model = new DefaultComboBoxModel( );
        employeeCombobox.setModel(model);
        JComponent[] inputs =
                new JComponent[] { myLabel1,
                        myLabel1,
                        employeeCombobox};
        int valor = JOptionPane.showConfirmDialog( null,
                inputs,
                "Message",
                JOptionPane.OK_CANCEL_OPTION);

        return 1;
    }

    public static
}
