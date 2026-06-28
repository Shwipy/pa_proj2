package view;

import javax.swing.*;

/**
 * Class to manage the multiple types of Dialogs that cam appear
 */
public class Shared {

    /**
     * Function to display an OK/Cancel type Dialog
     *
     * @param frame   frame of the dialog
     * @param message message of the dialog
     * @param title   of the dialog
     * @return result of the dialog
     */
    public static Boolean showOkCancelDialog(JFrame frame, String message, String title) {
        int dialogResult = JOptionPane.showConfirmDialog(
                frame,
                message,
                title,
                JOptionPane.OK_CANCEL_OPTION
        );

        return dialogResult == 0;
    }

    /**
     * Function to display an Yes/No type Dialog
     *
     * @param frame   frame of the dialog
     * @param message message of the dialog
     * @param title   of the dialog
     * @return result of the dialog (boolean)
     */
    public static Boolean showYesNoDialog(JFrame frame, String message, String title) {

        int dialogResult = JOptionPane.showConfirmDialog(
                frame,
                message,
                title,
                JOptionPane.YES_NO_OPTION
        );

        return dialogResult == JOptionPane.YES_OPTION;
    }

    /**
     * Function to display an Error type Dialog
     *
     * @param frame   frame of the dialog
     * @param message message of the dialog
     * @param title   of the dialog
     */
    public static void showErrorDialog(JFrame frame, String message, String title) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );

    }

    /**
     * Function to display an Information type Dialog
     *
     * @param frame   frame of the dialog
     * @param message message of the dialog
     * @param title   of the dialog
     */
    public static void showInformationDialog(JFrame frame, String message, String title) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Function to display an Information type Dialog
     *
     * @param frame   frame of the dialog
     * @param message message of the dialog
     * @param title   of the dialog
     * @return id of the selected employee
     */
    public static String showInputDialog(JFrame frame, String message, String title) {
        String employeeIdText = JOptionPane.showInputDialog(
                frame,
                message,
                title,
                JOptionPane.QUESTION_MESSAGE
        );

        return employeeIdText;
    }

    //work in progress( Era para dar para escolher com uma bombobox os employees que estavam disponiveis, mas acabei por não conseguir.. ;_;)
//    public static Integer showSelectEmployeeDialog(JFrame frame,String message, String title, Integer employeeId, Integer repairId){
//        JLabel myLabel1 = new JLabel("Select the Employee for this Repair:");
//        JComboBox employeeCombobox = new JComboBox();
//        //ArrayList<Employee> availableEmployeeList = EmployeeController.availableForRepair(employeeId,repairId);
//        DefaultComboBoxModel model = new DefaultComboBoxModel( );
//        employeeCombobox.setModel(model);
//        JComponent[] inputs =
//                new JComponent[] { myLabel1,
//                        myLabel1,
//                        employeeCombobox};
//        int valor = JOptionPane.showConfirmDialog( null,
//                inputs,
//                "Message",
//                JOptionPane.OK_CANCEL_OPTION);
//
//        return 1;
//    }


}
