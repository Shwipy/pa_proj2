package view;

import controller.NotificationController;
import controller.RepairController;
import model.Notification;
import model.Repair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class EmployeeView implements ActionListener, MouseListener {

    private Integer employeeId;

    private JButton repairSearchButton;
    private JButton notificationSearchButton;

    private JComboBox repairSearchTypes;

    private JTextField repairSearchTerm;

    private JTable repairTable;
    private JTable notificationTable;

    private JCheckBox notificationShowAllBox;

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

    private String [] notificationColumnNames = {
            "ID",
            "Message",
            "Read",
            "Creation Date",
            "Sender ID",
            "Type",
            "Repair ID"
    };

    public EmployeeView(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public JTabbedPane show(){
        JTabbedPane tabPanel = new JTabbedPane();

        JPanel repairPanel = new JPanel();
        repairPanel.setLayout(new BorderLayout());

        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BorderLayout());

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());

        //------------Repair Panel Main
        //https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames,0);

        ArrayList<Repair> repairs = RepairController.listRepairsByEmployee(employeeId);

        if (repairs != null) {
            for (Repair repair : repairs) {
                repairModel.addRow(repair.toTableRow());
            }
        }

        repairTable = new JTable(repairModel);
        //https://stackoverflow.com/questions/9919230/disable-user-edit-in-jtable/36747065#36747065
        repairTable.setDefaultEditor(Object.class, null);
        repairTable.setAutoCreateRowSorter(true);
        repairTable.addMouseListener(this);
        repairTable.removeColumn(repairTable.getColumnModel().getColumn(9));

        JScrollPane repairScrollPane = new JScrollPane(repairTable);

        JPanel repairSearchbar = new JPanel();
        repairSearchbar.setLayout(new GridLayout(1,5));

        JLabel repairSearchLabel = new JLabel("   Search: ", SwingConstants.RIGHT);
        repairSearchbar.add(repairSearchLabel);

        repairSearchTerm = new JTextField();
        repairSearchbar.add(repairSearchTerm);

        repairSearchTypes = new JComboBox<>(new String[]{
                "Number",
                "State"
        });
        repairSearchbar.add(repairSearchTypes);

        repairSearchbar.add(new JLabel());

        repairSearchButton = new JButton("Search");
        repairSearchButton.addActionListener(this);
        repairSearchbar.add(repairSearchButton);

        repairPanel.add(repairSearchbar, BorderLayout.NORTH);
        repairPanel.add(repairScrollPane, BorderLayout.CENTER);
        repairPanel.add(new JLabel("   Note: To view the details of a specific Repair, double click on the row!", SwingConstants.LEFT), BorderLayout.SOUTH);

        //------------Notification Panel Main
        //https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        DefaultTableModel notificationModel = new DefaultTableModel(notificationColumnNames,0);

        ArrayList<Notification> notifications =
                NotificationController.listNotifications(employeeId, false);

        if (notifications != null) {
            for (Notification notification : notifications) {
                notificationModel.addRow(notification.toTableRow());
            }
        }

        notificationTable = new JTable(notificationModel);
        //https://stackoverflow.com/questions/9919230/disable-user-edit-in-jtable/36747065#36747065
        notificationTable.setDefaultEditor(Object.class, null);
        notificationTable.setAutoCreateRowSorter(true);
        notificationTable.addMouseListener(this);
        notificationTable.removeColumn(notificationTable.getColumnModel().getColumn(1));

        JScrollPane notificationScrollPane = new JScrollPane(notificationTable);

        JPanel notificationSearchbar = new JPanel();
        notificationSearchbar.setLayout(new GridLayout(1,5));

        notificationSearchbar.add(new JLabel());
        notificationSearchbar.add(new JLabel());
        notificationSearchbar.add(new JLabel());

        JPanel notificationShowAllPanel = new JPanel();
        notificationShowAllPanel.setLayout(new GridLayout(1,2));

        JLabel notificationShowAllLabel =
                new JLabel("Show All: ", SwingConstants.RIGHT);
        notificationShowAllPanel.add(notificationShowAllLabel);

        notificationShowAllBox = new JCheckBox();
        notificationShowAllPanel.add(notificationShowAllBox);

        notificationSearchbar.add(notificationShowAllPanel);

        notificationSearchButton = new JButton("Search");
        notificationSearchButton.addActionListener(this);
        notificationSearchbar.add(notificationSearchButton);

        notificationPanel.add(notificationSearchbar, BorderLayout.NORTH);
        notificationPanel.add(notificationScrollPane, BorderLayout.CENTER);
        notificationPanel.add(new JLabel("   Note: To view the message of a specific Notification, double click on the row!", SwingConstants.LEFT), BorderLayout.SOUTH);

        tabPanel.addTab("Repairs", repairPanel);
        tabPanel.addTab("Notifications", notificationPanel);
        tabPanel.addTab("Settings", settingsPanel);

        settingsPanel.add(new InfoPanel(employeeId, "employee", true));

        return tabPanel;
    }

    public void fillRepairTable(Boolean search) {
        DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames,0);
        ArrayList<Repair> repairs;

        if (search) {
            repairs = RepairController.searchRepairsByEmployee(
                    repairSearchTerm.getText(),
                    repairSearchTypes.getSelectedItem().toString().toLowerCase(),
                    employeeId
            );
        } else {
            repairs = RepairController.listRepairsByEmployee(employeeId);
        }

        if (repairs != null) {
            for (Repair repair : repairs) {
                repairModel.addRow(repair.toTableRow());
            }
        }

        //https://stackoverflow.com/questions/10331949/setmodel-on-a-jtable-when-the-defaulttablemodel-has-changed
        repairTable.setModel(repairModel);

        repairTable.removeColumn(repairTable.getColumnModel().getColumn(9));
    }

    public void fillNotificationTable(Boolean showAll) {
        DefaultTableModel notificationModel =
                new DefaultTableModel(notificationColumnNames,0);

        ArrayList<Notification> notifications =
                NotificationController.listNotifications(employeeId, showAll);

        if (notifications != null) {
            for (Notification notification : notifications) {
                notificationModel.addRow(notification.toTableRow());
            }
        }

        //https://stackoverflow.com/questions/10331949/setmodel-on-a-jtable-when-the-defaulttablemodel-has-changed
        notificationTable.setModel(notificationModel);

        notificationTable.removeColumn(
                notificationTable.getColumnModel().getColumn(1)
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(repairSearchButton)){
            fillRepairTable(true);
        }

        if (e.getSource().equals(notificationSearchButton)){
            fillNotificationTable(notificationShowAllBox.isSelected());
        }
    }

    //https://coderanch.com/t/341705/java/JTable-responding-row-clicks
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(repairTable) && e.getClickCount() == 2) {
            System.out.println("Double click repair");

            int selectedRow = repairTable.getSelectedRow();

            if (selectedRow >= 0) {
                int modelRow = repairTable.convertRowIndexToModel(selectedRow);
                String notes = String.valueOf(repairTable.getModel().getValueAt(modelRow, 9));

                if (notes == null || notes.trim().equals("") || notes.equals("null")) {
                    notes = "This repair has no notes.";
                }

                Shared.showInformationDialog(
                        new JFrame(),
                        notes,
                        "Repair Notes"
                );
            }
        }

        if (e.getSource().equals(notificationTable) && e.getClickCount() == 2) {

            int selectedRow = notificationTable.getSelectedRow();

            if (selectedRow >= 0) {
                int modelRow =
                        notificationTable.convertRowIndexToModel(selectedRow);

                String message = String.valueOf(
                        notificationTable.getModel().getValueAt(modelRow, 1)
                );

                if (message == null
                        || message.trim().equals("") || message.equals("null")) {

                    message = "This notification has no message.";
                }

                Shared.showInformationDialog(
                        new JFrame(),
                        message,
                        "Notification Message"
                );
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
