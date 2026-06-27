package view;

import controller.EquipmentController;
import controller.NotificationController;
import controller.RepairController;
import model.Equipment;
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
 * Class to manage what a client sees on the app
 */
public class ClientView implements ActionListener, MouseListener {

    private Integer clientId;

    private JButton equipmentSearchButton;
    private JButton repairSearchButton;
    private JButton notificationSearchButton;
    private JButton addEquipmentButton;
    private JButton addRepairButton;

    private JComboBox equipmentSearchTypes;
    private JComboBox repairSearchTypes;

    private JTextField equipmentSearchTerm;
    private JTextField repairSearchTerm;

    private JTable equipmentTable;
    private JTable repairTable;
    private JTable notificationTable;

    private JCheckBox notificationShowAllBox;

    private String[] equipmentColumnNames = {
            "ID",
            "Client ID",
            "Brand",
            "Model",
            "SKU",
            "Batch",
            "Manufacture Date",
            "Submission Date",
            "Repair Date"
    };

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

    public ClientView(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * Function to show the graphical interface
     */
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

        //------------Equipment Panel Main
        //https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        DefaultTableModel equipmentModel = new DefaultTableModel(equipmentColumnNames, 0);

        ArrayList<Equipment> equipments = EquipmentController.listEquipmentsByClient(clientId);

        if (equipments != null) {
            for (Equipment equipment : equipments) {
                equipmentModel.addRow(equipment.toTableRow());
            }
        }

        equipmentTable = new JTable(equipmentModel);
        //https://stackoverflow.com/questions/9919230/disable-user-edit-in-jtable/36747065#36747065
        equipmentTable.setDefaultEditor(Object.class, null);
        equipmentTable.setAutoCreateRowSorter(true);
        //equipmentTable.setFillsViewportHeight(true);

        JScrollPane equipmentScrollPane = new JScrollPane(equipmentTable);

        JPanel equipmentSearchbar = new JPanel();
        equipmentSearchbar.setLayout(new GridLayout(1, 5));

        JLabel equipmentSearchLabel = new JLabel("   Search: ", SwingConstants.RIGHT);
        equipmentSearchbar.add(equipmentSearchLabel);

        equipmentSearchTerm = new JTextField();
        equipmentSearchbar.add(equipmentSearchTerm);

        equipmentSearchTypes = new JComboBox<>(new String[]{
                "Brand",
                "SKU"
        });

        equipmentSearchbar.add(equipmentSearchTypes);

        addEquipmentButton = new JButton("Add Equipment");
        addEquipmentButton.setToolTipText(
                "Open the form to add a new equipment."
        );

        addEquipmentButton.addActionListener(this);
        equipmentSearchbar.add(addEquipmentButton);
        equipmentSearchButton = new JButton("Search");
        equipmentSearchButton.addActionListener(this);
        equipmentSearchbar.add(equipmentSearchButton);

        equipmentPanel.add(equipmentSearchbar, BorderLayout.NORTH);
        equipmentPanel.add(equipmentScrollPane, BorderLayout.CENTER);
        equipmentPanel.add(new JLabel("   Note: To view the details of a specific Equipment, double click on the row!", SwingConstants.LEFT), BorderLayout.SOUTH);

        //------------Repair Panel Main
        //https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames, 0);

        ArrayList<Repair> repairs = RepairController.listRepairsByClient(clientId);

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

        addRepairButton = new JButton("Add Repair");
        addRepairButton.addActionListener(this);
        repairSearchbar.add(addRepairButton);

        repairSearchButton = new JButton("Search");
        repairSearchButton.addActionListener(this);
        repairSearchbar.add(repairSearchButton);

        repairPanel.add(repairSearchbar, BorderLayout.NORTH);
        repairPanel.add(repairScrollPane, BorderLayout.CENTER);
        repairPanel.add(new JLabel("   Note: To view the details of a specific Repair, double click on the row!", SwingConstants.LEFT), BorderLayout.SOUTH);

        //------------Notification Panel Main
        //https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        DefaultTableModel notificationModel = new DefaultTableModel(notificationColumnNames, 0);

        ArrayList<Notification> notifications = NotificationController.listNotifications(clientId, false);

        if (notifications != null) {
            if (!notifications.isEmpty()) {
                Shared.showInformationDialog(new JFrame(), "YOU GOT MAIL", "New Notifications");
            }
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
        notificationSearchbar.setLayout(new GridLayout(1, 5));

        notificationSearchbar.add(new JLabel());
        notificationSearchbar.add(new JLabel());
        notificationSearchbar.add(new JLabel());

        JPanel notificationShowAllPanel = new JPanel();
        notificationShowAllPanel.setLayout(new GridLayout(1, 2));

        JLabel notificationShowAllLabel = new JLabel("Show All: ", SwingConstants.RIGHT);
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

        tabPanel.addTab("Equipments", equipmentPanel);
        tabPanel.addTab("Repairs", repairPanel);
        tabPanel.addTab("Notifications", notificationPanel);
        tabPanel.addTab("Settings", settingsPanel);

        settingsPanel.add(new InfoPanel(clientId, "client", true));
        return tabPanel;
    }

    /**
     * Fills the repair equipment
     * @param search Boolean to indicate if we're searching for something
     */
    public void fillEquipmentTable(Boolean search) {
        DefaultTableModel equipmentModel = new DefaultTableModel(equipmentColumnNames, 0);
        ArrayList<Equipment> equipments;

        if (search) {
            equipments = EquipmentController.searchEquipmentsByClient(
                    equipmentSearchTerm.getText(),
                    equipmentSearchTypes.getSelectedItem().toString().toLowerCase(),
                    clientId
            );
        } else {
            equipments = EquipmentController.listEquipmentsByClient(clientId);
        }

        if (equipments != null) {
            for (Equipment equipment : equipments) {
                equipmentModel.addRow(equipment.toTableRow());
            }
        }

        //https://stackoverflow.com/questions/10331949/setmodel-on-a-jtable-when-the-defaulttablemodel-has-changed
        equipmentTable.setModel(equipmentModel);
    }

    /**
     * Fills the repair table
     * @param search Boolean to indicate if we're searching for something
     */
    public void fillRepairTable(Boolean search) {
        DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames, 0);
        ArrayList<Repair> repairs;

        if (search) {
            repairs = RepairController.searchRepairsByClient(
                    repairSearchTerm.getText(),
                    repairSearchTypes.getSelectedItem().toString().toLowerCase(),
                    clientId
            );
        } else {
            repairs = RepairController.listRepairsByClient(clientId);
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
        DefaultTableModel notificationModel = new DefaultTableModel(notificationColumnNames, 0);

        ArrayList<Notification> notifications = NotificationController.listNotifications(clientId, showAll);

        if (notifications != null) {
            for (Notification notification : notifications) {
                notificationModel.addRow(notification.toTableRow());
            }
        }

        //https://stackoverflow.com/questions/10331949/setmodel-on-a-jtable-when-the-defaulttablemodel-has-changed
        notificationTable.setModel(notificationModel);
        notificationTable.removeColumn(notificationTable.getColumnModel().getColumn(1));
    }

    /**
     * Displays the form to add a new Equipment
     */
    private void showEquipmentWindow() {

        JDialog dialog = new JDialog();

        JPanel dialogWindow = new EquipmentWindow(
                clientId,
                this
        );

        dialog.setSize(600, 400);

        dialog.setModalityType(
                Dialog.ModalityType.APPLICATION_MODAL
        );

        dialog.add(dialogWindow);
        dialog.setTitle("Add Equipment");
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);

        fillEquipmentTable(false);
    }

    /**
     * Displays the form to add a new Repair
     */
    private void showRepairWindow() {

        JDialog dialog = new JDialog();

        JPanel dialogWindow = new RepairWindow(
                clientId,
                this
        );

        dialog.setSize(600, 400);

        dialog.setModalityType(
                Dialog.ModalityType.APPLICATION_MODAL
        );

        dialog.add(dialogWindow);
        dialog.setTitle("Add Repair");
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);

        fillRepairTable(false);
    }

    /**
     * Checks when an action is performed
     * @param e used to identify which button was pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(addEquipmentButton)) {
            showEquipmentWindow();
        }

        if (e.getSource().equals(addRepairButton)) {
            showRepairWindow();
        }

        if (e.getSource().equals(equipmentSearchButton)) {
            fillEquipmentTable(true);
        }

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

                int modelRow = notificationTable.convertRowIndexToModel(selectedRow);

                Integer notificationId = (Integer) notificationTable.getModel().getValueAt(modelRow, 0);

                Notification notification = NotificationController.getNotification(notificationId, clientId);

                if (notification == null) {

                    Shared.showErrorDialog(
                            new JFrame(),
                            "This notification is no longer available.",
                            "Notification Error"
                    );

                    return;
                }

                //--------------------------------------Repair Rejected or Finished--------------------------------------
                if (notification.getType().equals("repairRejected") || notification.getType().equals("repairFinished")) {

                    String message = notification.getMessage();

                    if (message == null || message.trim().equals("") || message.equals("null")) {
                        message = "This notification has no message.";
                    }

                    Shared.showInformationDialog(
                            new JFrame(),
                            message,
                            "Notification Message"
                    );

                    NotificationController.maskAsRead(notification);

                    fillNotificationTable(notificationShowAllBox.isSelected());

                    //--------------------------------------Other Notifications--------------------------------------
                } else {

                    String message = notification.getMessage();

                    if (message == null || message.trim().equals("") || message.equals("null")) {

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

    /**
     * Mouse events not used
     * @param e used to identify mouse event
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
