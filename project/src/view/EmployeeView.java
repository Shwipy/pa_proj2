package view;

import controller.RepairController;
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

    private JComboBox repairSearchTypes;

    private JTextField repairSearchTerm;

    private JTable repairTable;

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

        tabPanel.addTab("Repairs", repairPanel);
        tabPanel.addTab("Notifications", notificationPanel);
        tabPanel.addTab("Settings", settingsPanel);

        settingsPanel.add(new InfoPanel(employeeId, "employee", true));
        return tabPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(repairSearchButton)){
            DefaultTableModel repairModel = new DefaultTableModel(repairColumnNames,0);

            ArrayList<Repair> repairs = RepairController.searchRepairsByEmployee(
                    repairSearchTerm.getText(),
                    repairSearchTypes.getSelectedItem().toString().toLowerCase(),
                    employeeId
            );

            if (repairs != null) {
                for (Repair repair : repairs) {
                    repairModel.addRow(repair.toTableRow());
                }
            }

            //https://stackoverflow.com/questions/10331949/setmodel-on-a-jtable-when-the-defaulttablemodel-has-changed
            repairTable.setModel(repairModel);
            repairTable.removeColumn(repairTable.getColumnModel().getColumn(9));
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
