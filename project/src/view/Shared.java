package view;

import javax.swing.*;

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
}
