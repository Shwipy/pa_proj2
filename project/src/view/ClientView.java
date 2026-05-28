package view;

import controller.EquipmentController;
import model.Equipment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ClientView implements ActionListener {

    private Integer clientId;

    private JButton equipmentSearchButton;

    private JComboBox equipmentSearchTypes;

    private JTextField equipmentSearchTerm;

    private JTable equipmentTable;

    private String [] equipmentColumnNames = {
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

    public ClientView(Integer clientId) {
        this.clientId = clientId;
    }

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
        DefaultTableModel equipmentModel = new DefaultTableModel(equipmentColumnNames,0);

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
        equipmentSearchbar.setLayout(new GridLayout(1,5));

        JLabel equipmentSearchLabel = new JLabel("   Search: ", SwingConstants.RIGHT);
        equipmentSearchbar.add(equipmentSearchLabel);

        equipmentSearchTerm = new JTextField();
        equipmentSearchbar.add(equipmentSearchTerm);

        equipmentSearchTypes = new JComboBox<>(new String[]{
                "Brand",
                "SKU"
        });
        equipmentSearchbar.add(equipmentSearchTypes);

        equipmentSearchbar.add(new JLabel());

        equipmentSearchButton = new JButton("Search");
        equipmentSearchButton.addActionListener(this);
        equipmentSearchbar.add(equipmentSearchButton);

        equipmentPanel.add(equipmentSearchbar, BorderLayout.NORTH);
        equipmentPanel.add(equipmentScrollPane, BorderLayout.CENTER);
        equipmentPanel.add(new JLabel("   Note: To view the details of a specific Equipment, double click on the row!", SwingConstants.LEFT), BorderLayout.SOUTH);

        tabPanel.addTab("Equipments", equipmentPanel);
        tabPanel.addTab("Repairs", repairPanel);
        tabPanel.addTab("Notifications", notificationPanel);
        tabPanel.addTab("Settings", settingsPanel);

        return tabPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(equipmentSearchButton)){
            DefaultTableModel equipmentModel = new DefaultTableModel(equipmentColumnNames,0);

            ArrayList<Equipment> equipments = EquipmentController.searchEquipmentsByClient(
                    equipmentSearchTerm.getText(),
                    equipmentSearchTypes.getSelectedItem().toString().toLowerCase(),
                    clientId
            );

            if (equipments != null) {
                for (Equipment equipment : equipments) {
                    equipmentModel.addRow(equipment.toTableRow());
                }
            }

            equipmentTable.setModel(equipmentModel);
        }
    }
}
