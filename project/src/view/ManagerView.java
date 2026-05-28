package view;

import controller.ManagerController;
import controller.RepairController;
import model.Manager;
import model.Repair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManagerView implements ActionListener {
    private JButton accountSearchButton;
    private JButton repairSearchButton;

    private JComboBox accountSearchTypes;
    private JComboBox repairSearchTypes;

    private JTextField accountSearchTerm;
    private JTextField repairSearchTerm;

    private JTable accountTable;
    private JTable repairTable;

    private JCheckBox repairCheckFinishedBox;

    private String [] accountColumnNames = {
            "ID",
            "Name",
            "Username",
            "Active",
            "Approved" ,
            "Email",
            "Type"
    };

    private String [] repairColumnNames = {
            "ID",
            "Repair Number",
            "Employee ID",
            "Equipment ID",
            "Start Date",
            "End Date",
            "Duration",
            "Price",
            "State",
            "Notes"
    };

    public JTabbedPane show(){
        JTabbedPane tabPanel = new JTabbedPane();

        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BorderLayout());

        JPanel repairPanel = new JPanel();
        repairPanel.setLayout(new BorderLayout());

        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BorderLayout());

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());

        //------------Account Panel Main
        //https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        DefaultTableModel accountModel = new DefaultTableModel(accountColumnNames,0);

        ArrayList<Manager> users = ManagerController.listUsers();

        if (users != null){
            for (Manager user : users) {
                accountModel.addRow(user.toTableRow());
            }
        }

        accountTable = new JTable(accountModel);
        //https://stackoverflow.com/questions/9919230/disable-user-edit-in-jtable/36747065#36747065
        accountTable.setDefaultEditor(Object.class, null);
        accountTable.setAutoCreateRowSorter(true);
        //table.setFillsViewportHeight(true);

        JScrollPane accountScrollPane = new JScrollPane(accountTable);

        JPanel accountSearchbar = new JPanel();
        accountSearchbar.setLayout(new GridLayout(1,5));

        JLabel accountSearchLabel = new JLabel("   Search: ",SwingConstants.RIGHT);
        accountSearchbar.add(accountSearchLabel);

        accountSearchTerm = new JTextField();
        accountSearchbar.add(accountSearchTerm);

        accountSearchTypes = new JComboBox<>(new String[]{
                "Name",
                "Username",
                "Type"
        });
        accountSearchbar.add(accountSearchTypes);

        accountSearchbar.add(new JLabel());

        accountSearchButton = new JButton("Search");
        accountSearchButton.addActionListener(this);
        accountSearchbar.add(accountSearchButton);

        accountPanel.add(accountSearchbar, BorderLayout.NORTH);
        accountPanel.add(accountScrollPane, BorderLayout.CENTER);
        accountPanel.add(new JLabel("   Note: To view the details of a specific User, double click on the row!", SwingConstants.LEFT), BorderLayout.SOUTH);

        //------------Repair Panel Main

        DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames,0);

        ArrayList<Repair> repairs = RepairController.listAllRepairs();

        if (repairs != null) {
            for (Repair repair : repairs) {
                repairModel.addRow(repair.toTableRow());
            }
        }

        repairTable = new JTable(repairModel);
        repairTable.setDefaultEditor(Object.class, null);
        repairTable.setAutoCreateRowSorter(true);
        //repairTable.setFillsViewportHeight(true);

        JScrollPane repairScrollPane = new JScrollPane(repairTable);

        JPanel repairSearchbar = new JPanel();
        repairSearchbar.setLayout(new GridLayout(1,5));

        JLabel repairSearchLabel = new JLabel("   Search: ", SwingConstants.RIGHT);
        repairSearchbar.add(repairSearchLabel);

        repairSearchTerm = new JTextField();
        repairSearchbar.add(repairSearchTerm);

        repairSearchTypes = new JComboBox<>(new String[]{
                "Repair Number",
                "Repair State",
                "Client Name"
        });
        repairSearchbar.add(repairSearchTypes);

        JPanel repairCheckFinishedPanel = new JPanel();
        repairCheckFinishedPanel.setLayout(new GridLayout(1,2));

        JLabel repairCheckFinishedLabel = new JLabel("Incomplete: ",SwingConstants.RIGHT);
        repairCheckFinishedPanel.add(repairCheckFinishedLabel);

        repairCheckFinishedBox = new JCheckBox();
        repairCheckFinishedPanel.add(repairCheckFinishedBox);

        repairSearchbar.add(repairCheckFinishedPanel);

        repairSearchButton = new JButton("Search");
        repairSearchButton.addActionListener(this);
        repairSearchbar.add(repairSearchButton);

        repairPanel.add(repairSearchbar, BorderLayout.NORTH);
        repairPanel.add(repairScrollPane, BorderLayout.CENTER);
        repairPanel.add(new JLabel("   Note: To view the details of a specific Repair, double click on the row!", SwingConstants.LEFT), BorderLayout.SOUTH);

        tabPanel.addTab("Accounts", accountPanel);
        tabPanel.addTab("Repairs", repairPanel);
        tabPanel.addTab("Notifications", notificationPanel);
        tabPanel.addTab("Settings", settingsPanel);

        return tabPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(accountSearchButton)){
            DefaultTableModel accountModel = new DefaultTableModel(accountColumnNames,0);

            ArrayList<Manager> users = ManagerController.searchUsers(accountSearchTerm.getText(),accountSearchTypes.getSelectedItem().toString().toLowerCase());

            if (users != null){
                for (Manager user : users) {
                    accountModel.addRow(user.toTableRow());
                }
            }

            //https://stackoverflow.com/questions/10331949/setmodel-on-a-jtable-when-the-defaulttablemodel-has-changed
            accountTable.setModel(accountModel);

        }

        if (e.getSource().equals(repairSearchButton)){
            DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames,0);
            ArrayList<Repair> repairs = RepairController.searchRepairs(repairSearchTerm.getText(), repairSearchTypes.getSelectedItem().toString().toLowerCase(), repairCheckFinishedBox.isSelected());

            if (repairs != null){
                for (Repair repair : repairs) {
                    repairModel.addRow(repair.toTableRow());
                }
            }

            //https://stackoverflow.com/questions/10331949/setmodel-on-a-jtable-when-the-defaulttablemodel-has-changed
            repairTable.setModel(repairModel);
        }

        //need to change to try on a MouseAdapter
        //https://stackoverflow.com/questions/14852719/double-click-listener-on-jtable-in-java
        if (e.getSource().equals(accountTable)){
            System.out.println("qwe");
            System.out.println(accountTable.getSelectedRow());
        }
    }
}
