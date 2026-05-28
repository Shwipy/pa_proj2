package view;

import javax.swing.*;
import java.awt.*;

public class EmployeeView {

    public JTabbedPane show(){
        JTabbedPane tabPanel = new JTabbedPane();

        JPanel repairPanel = new JPanel();
        repairPanel.setLayout(new BorderLayout());

        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BorderLayout());

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());

        tabPanel.addTab("Repairs", repairPanel);
        tabPanel.addTab("Notifications", notificationPanel);
        tabPanel.addTab("Settings", settingsPanel);

        return tabPanel;
    }
}
