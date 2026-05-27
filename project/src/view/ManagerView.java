package view;

import controller.ManagerController;
import model.Manager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicColorChooserUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

public class ManagerView {

    public static JTabbedPane show(){
        JTabbedPane tabPanel = new JTabbedPane();

        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BorderLayout());
        JPanel repairPanel = new JPanel();
        repairPanel.setLayout(new BorderLayout());
        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BorderLayout());
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());

        accountPanel.add(showAccountPanel(), BorderLayout.CENTER);

        tabPanel.addTab("Accounts", accountPanel);
        tabPanel.addTab("Repairs", repairPanel);
        tabPanel.addTab("Notifications", notificationPanel);
        tabPanel.addTab("Settings", settingsPanel);
        return tabPanel;
    }

    public static JPanel showAccountPanel(){
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BorderLayout());

        String [] columnNames = { "ID", "Name", "Username", "Active", "Approved" , "Email", "Type" };

        //https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        DefaultTableModel model = new DefaultTableModel(columnNames,0);

        ArrayList<Manager> users = ManagerController.listUsers();

        for (Manager user : users) {
            model.addRow(user.toTableRow());
        }

        JTable table = new JTable(model);
        //https://stackoverflow.com/questions/9919230/disable-user-edit-in-jtable/36747065#36747065
        table.setDefaultEditor(Object.class, null);
        table.setAutoCreateRowSorter(true);
        //table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        accountPanel.add(scrollPane, BorderLayout.CENTER);
        accountPanel.add(new JLabel("   Search: ________________________  |DROPDOWN MENU para escolher o termo de pesquisa|  |Search Button|", SwingConstants.LEFT), BorderLayout.NORTH);
        accountPanel.add(new JLabel("   Note: To view the details of a specific User, double click on the row!", SwingConstants.LEFT), BorderLayout.SOUTH);

        return  accountPanel;
    }
}
