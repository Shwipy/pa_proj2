package view;

import controller.ManagerController;
import controller.RepairController;
import model.Manager;
import model.Repair;

import javax.swing.*;
import javax.swing.plaf.basic.BasicColorChooserUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManagerView implements ActionListener {
    public JButton searchButton;
    public JButton repairSearchButton;

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

        JPanel searchbar = new JPanel();
        searchbar.setLayout(new GridLayout(1,5));

        JLabel searchLabel = new JLabel("   Search: ",SwingConstants.RIGHT);
        searchbar.add(searchLabel);

        JTextField searchTerm = new JTextField();
        searchbar.add(searchTerm);

        JComboBox searchTypes = new JComboBox<>(new String[]{"Name","Username","Type"});
        searchbar.add(searchTypes);

        searchbar.add(new JLabel());

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchbar.add(searchButton);

        accountPanel.add(searchbar, BorderLayout.NORTH);
        accountPanel.add(scrollPane, BorderLayout.CENTER);
        accountPanel.add(new JLabel("   Note: To view the details of a specific User, double click on the row!", SwingConstants.LEFT), BorderLayout.SOUTH);

        //------------Repair Panel Main
        String [] repairColumnNames = { "ID", "Repair Number", "Employee ID", "Equipment ID", "Start Date", "End Date", "Duration", "Price", "State", "Notes" };

        //https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames,0);

        ArrayList<Repair> repairs = RepairController.listAllRepairs();

        if (repairs != null) {
            for (Repair repair : repairs) {
                repairModel.addRow(repair.toTableRow());
            }
        }

        JTable repairTable = new JTable(repairModel);
        //https://stackoverflow.com/questions/9919230/disable-user-edit-in-jtable/36747065#36747065
        repairTable.setDefaultEditor(Object.class, null);
        repairTable.setAutoCreateRowSorter(true);
        //repairTable.setFillsViewportHeight(true);

        JScrollPane repairScrollPane = new JScrollPane(repairTable);

        JPanel repairSearchbar = new JPanel();
        repairSearchbar.setLayout(new GridLayout(1,5));

        JLabel repairSearchLabel = new JLabel("   Search: ", SwingConstants.RIGHT);
        repairSearchbar.add(repairSearchLabel);

        JTextField repairSearchTerm = new JTextField();
        repairSearchbar.add(repairSearchTerm);

        JComboBox repairSearchTypes = new JComboBox<>(new String[]{
                "Repair Number",
                "Repair State",
                "Client Name",
                "Equipment Brand",
                "Equipment Code"
        });
        repairSearchbar.add(repairSearchTypes);

        repairSearchbar.add(new JLabel());

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

    public static JPanel showAccountPanel(){
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BorderLayout());

        return  accountPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(searchButton)){
            System.out.println("NICE");
        }

        if (e.getSource().equals(repairSearchButton)){
            System.out.println("REPAIR SEARCH");
        }
    }
}
