package view;

import javax.swing.*;

public class Shared {

    public static Boolean showDialog(JFrame frame, String message, String title){
        int dialogResult = JOptionPane.showConfirmDialog(
                frame,
                message,
                title,
                JOptionPane.OK_CANCEL_OPTION
        );

        return dialogResult == 0;
    }
}
