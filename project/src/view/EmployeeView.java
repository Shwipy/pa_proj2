package view;

import controller.InputController;
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

/**
 * Class to manage what an employee sees on the app
 */
public class EmployeeView implements ActionListener, MouseListener {

    private Integer employeeId;

    private JButton repairSearchButton;
    private JButton notificationSearchButton;

    private JComboBox repairSearchTypes;

    private JTextField repairSearchTerm;

    private JTable repairTable;
    private JTable notificationTable;

    private JCheckBox notificationShowAllBox;

    private String[] repairColumnNames = {
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

    private String[] notificationColumnNames = {
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

    /**
     * Function to show the graphical interface
     */
    public JTabbedPane show() {
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
        DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames, 0);

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
        repairSearchbar.setLayout(new GridLayout(1, 5));

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
        DefaultTableModel notificationModel = new DefaultTableModel(notificationColumnNames, 0);

        ArrayList<Notification> notifications =
                NotificationController.listNotifications(employeeId, false);

        if (notifications != null) {
            if (!notifications.isEmpty()) {
                Shared.showInformationDialog(new JFrame(), "YOU GOT MAIL", "New Notifications");
            }
            for (Notification notification : notifications) {
                notificationModel.addRow(notification.toTableRow());
            }
        }

        notificationTable = new JTable(notificationModel);
        notificationTable.setDefaultEditor(Object.class, null);
        notificationTable.setAutoCreateRowSorter(true);
        notificationTable.addMouseListener(this);
        notificationTable.removeColumn(notificationTable.getColumnModel().getColumn(1));

        JScrollPane notificationScrollPane = new JScrollPane(notificationTable);

        JPanel notificationSearchbar = new JPanel();
        notificationSearchbar.setLayout(new GridLayout(1, 5));

        notificationSearchbar.add(new JLabel());
        notificationSearchbar.add(new JLabel());
        notificationSearchbar.add(new JLabel());

        JPanel notificationShowAllPanel = new JPanel();
        notificationShowAllPanel.setLayout(new GridLayout(1, 2));

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

    /**
     * Fills the repair equipment
     * @param search Boolean to indicate if we're searching for something
     */
    public void fillRepairTable(Boolean search) {
        DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames, 0);
        ArrayList<Repair> repairs;

        if (search) {
            repairs = RepairController.searchRepairsByEmployee(repairSearchTerm.getText(), repairSearchTypes.getSelectedItem().toString().toLowerCase(), employeeId);
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

    /**
     * Updates the values of the rows of the notification table
     * @param showAll Boolean to indicate if we're showing all notifications or just the unread ones
     */
    public void fillNotificationTable(Boolean showAll) {
        DefaultTableModel notificationModel =
                new DefaultTableModel(notificationColumnNames, 0);

        ArrayList<Notification> notifications =
                NotificationController.listNotifications(employeeId, showAll);

        if (notifications != null) {
            for (Notification notification : notifications) {
                notificationModel.addRow(notification.toTableRow());
            }
        }

        notificationTable.setModel(notificationModel);

        notificationTable.removeColumn(notificationTable.getColumnModel().getColumn(1));
    }

    /**
     * Checks when an action is performed
     * @param e used to identify which button was pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(repairSearchButton)) {
            fillRepairTable(true);
        }

        if (e.getSource().equals(notificationSearchButton)) {
            fillNotificationTable(notificationShowAllBox.isSelected());
        }
    }

    /**
     * Checks when an action is performed
     * @param e used to identify mouse clicked
     */
    //https://coderanch.com/t/341705/java/JTable-responding-row-clicks
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(repairTable) && e.getClickCount() == 2) {

            Integer selectedRow = repairTable.getSelectedRow();

            if (selectedRow >= 0) {
                Integer modelRow = repairTable.convertRowIndexToModel(selectedRow);
                //Esquecemo-nos de fazer o finish!!!
//                String notes = String.valueOf(repairTable.getModel().getValueAt(modelRow, 9));
//
//                if (notes == null || notes.trim().equals("") || notes.equals("null")) {
//                    notes = "This repair has no notes.";
//                }
//
//                Shared.showInformationDialog(new JFrame(), notes, "Repair Notes");

                Integer repairId = (Integer) repairTable.getModel().getValueAt(modelRow, 0);
                String status = String.valueOf(repairTable.getModel().getValueAt(modelRow, 8));
                System.out.println(status);

                if (!status.equals("in_progress")) {
                    Shared.showErrorDialog(new JFrame(), "Can't finish this repair", "Invalid Status");
                    return;
                }

                Boolean answer = Shared.showYesNoDialog(new JFrame(), "Do you want to finish this repair?", "Finish Repair");

                if (answer) {
                    String inPrice = Shared.showInputDialog(new JFrame(), "Enter the repair price:", "Repair Price");

                    if (inPrice != null && !inPrice.trim().isEmpty()) {

                        Double price = InputController.readDouble(inPrice);

                        if (price < 0) {
                            Shared.showErrorDialog(new JFrame(), "Please enter a valid price!", "Invalid Input");
                            return;
                        }

                        if (RepairController.finishRepair(repairId, employeeId, "", price)) {
                            Shared.showInformationDialog(new JFrame(), "Repair finished successfully!", "Success");

                            fillNotificationTable(false);
                            fillRepairTable(notificationShowAllBox.isSelected());
                        } else {
                            Shared.showErrorDialog(new JFrame(), "There was a problem finishing the repair.", "Operation Error"
                            );
                        }
                    }
                }
            }
        }

        if (e.getSource().equals(notificationTable) && e.getClickCount() == 2) {

            int selectedRow = notificationTable.getSelectedRow();

            if (selectedRow >= 0) {

                int modelRow = notificationTable.convertRowIndexToModel(selectedRow);

                Integer notificationId = (Integer) notificationTable.getModel().getValueAt(modelRow, 0);

                Notification notification = NotificationController.getNotification(notificationId, employeeId);

                if (notification == null) {

                    Shared.showErrorDialog(new JFrame(), "This notification is no longer available.", "Notification Error");

                    return;
                }

                //--------------------------------------Assign Employee--------------------------------------
                if (notification.getType().equals("assignEmployee")) {

                    Boolean answer = Shared.showYesNoDialog(new JFrame(), notification.getMessage(), "Repair Assignment");

                    //--------------------------------------Accept Repair--------------------------------------
                    if (answer) {
                        if (RepairController.acceptRepair(notification.getRepairId(), notification.getAccountId())) {
                            NotificationController.maskAsRead(notification);

                            Shared.showInformationDialog(new JFrame(), "You were assigned to the repair!", "Valid Operation");

                            fillRepairTable(false);
                            fillNotificationTable(notificationShowAllBox.isSelected());

                        } else {

                            Shared.showErrorDialog(new JFrame(), "There was a problem accepting the repair.", "Operation Error");
                        }

                        //--------------------------------------Reject Assignment--------------------------------------
                    } else {

                        NotificationController.dennyRepair(notification.getAccountId(), notification.getRepairId());

                        NotificationController.maskAsRead(notification);

                        Shared.showInformationDialog(new JFrame(), "You declined being assigned to the repair!", "Valid Operation");

                        fillNotificationTable(notificationShowAllBox.isSelected());
                    }

                    //--------------------------------------Other Notifications--------------------------------------
                } else {

                    String message = notification.getMessage();

                    if (message == null || message.trim().equals("") || message.equals("null")) {
                        message = "This notification has no message.";
                    }

                    Shared.showInformationDialog(new JFrame(), message, "Notification Message");
                }
            }
        }
    }

    /**
     * Mouse events not used
     * @param e used to identify mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Mouse events not used
     * @param e used to identify mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Mouse events not used
     * @param e used to identify mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**s
     * Mouse events not used
     * @param e used to identify mouse event
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
