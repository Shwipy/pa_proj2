package view;

import javax.swing.*;
import java.awt.*;

public class ClientView {

    public JTabbedPane show() {
        JTabbedPane tabPanel = new JTabbedPane();

        JPanel equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new BorderLayout());

        JPanel repairPanel = new JPanel();
        repairPanel.setLayout(new BorderLayout());

        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BorderLayout());

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());

        tabPanel.addTab("Equipments", equipmentPanel);
        tabPanel.addTab("Repairs", repairPanel);
        tabPanel.addTab("Notifications", notificationPanel);
        tabPanel.addTab("Settings", settingsPanel);

        return tabPanel;
    }
}
