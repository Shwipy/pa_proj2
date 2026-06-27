package view;
import controller.*;
import model.Repair;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RepairWindow extends JPanel implements ActionListener {

    private Integer clientId;
    private ClientView clientView;

    private JTextField equipmentIdField;
    private JTextArea notesArea;

    private JButton createButton;

    public RepairWindow(Integer clientId, ClientView clientView) {

        this.clientId = clientId;
        this.clientView = clientView;

        this.setLayout(new BorderLayout());

        //--------------------------------------Center--------------------------------------
        JPanel repairPanel = new JPanel(new GridLayout(6, 1));

        JPanel titlePanel = new JPanel();

        JLabel titleLabel = new JLabel(
                "ADD REPAIR",
                SwingConstants.CENTER
        );

        titlePanel.add(titleLabel);

        JPanel equipmentIdPanel = new JPanel();

        JLabel equipmentIdLabel = new JLabel(
                "Equipment ID: ",
                SwingConstants.RIGHT
        );

        equipmentIdField = new JTextField(24);

        equipmentIdPanel.add(equipmentIdLabel);
        equipmentIdPanel.add(equipmentIdField);

        JPanel notesPanel = new JPanel();

        JLabel notesLabel = new JLabel(
                "Notes:              ",
                SwingConstants.RIGHT
        );

        notesArea = new JTextArea(4, 24);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        JScrollPane notesScrollPane = new JScrollPane(notesArea);

        notesPanel.add(notesLabel);
        notesPanel.add(notesScrollPane);

        repairPanel.add(titlePanel);
        repairPanel.add(new JPanel());
        repairPanel.add(equipmentIdPanel);
        repairPanel.add(notesPanel);
        repairPanel.add(new JPanel());
        repairPanel.add(new JPanel());

        //--------------------------------------Footer--------------------------------------
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new GridLayout(1, 4));

        createButton = new JButton("Create Repair");

        createButton.setToolTipText(
                "Press to validate and create a new repair."
        );

        createButton.addActionListener(this);

        footerPanel.add(new JPanel());
        footerPanel.add(new JPanel());
        footerPanel.add(new JPanel());
        footerPanel.add(createButton);

        //--------------------------------------Main Panel--------------------------------------
        JScrollPane scrollPane = new JScrollPane(repairPanel);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void validateFields() {

        Boolean inserted;
        Integer equipmentId;

        if (equipmentIdField.getText().trim().isEmpty()) {

            Shared.showErrorDialog(
                    new JFrame(),
                    "Equipment ID field cannot be empty!",
                    "Invalid Form"
            );

            return;
        }

        try {

            equipmentId = Integer.parseInt(
                    equipmentIdField.getText().trim()
            );

        } catch (NumberFormatException e) {

            Shared.showErrorDialog(
                    new JFrame(),
                    "Equipment ID must be a valid number!",
                    "Invalid Form"
            );

            return;
        }

        if (!EquipmentController.existsEquipmentForClient(
                equipmentId,
                clientId
        )) {

            Shared.showErrorDialog(
                    new JFrame(),
                    "This equipment does not belong to this client!",
                    "Invalid Equipment"
            );

            return;
        }

        if (RepairController.equipmentHasActiveRepair(equipmentId)) {

            Shared.showErrorDialog(
                    new JFrame(),
                    "This equipment already has an active repair!",
                    "Invalid Repair"
            );

            return;
        }

        String repairNumber=RepairController.generateRepairNumber();

        if (repairNumber == null) {

            Shared.showErrorDialog(
                    new JFrame(),
                    "There was an error generating the repair number.",
                    "Validation Error"
            );

            return;
        }

        Repair newRepair = new Repair(
                -1,
                null,
                equipmentId,
                repairNumber,
                System.currentTimeMillis() / 1000,
                null,
                null,
                null,
                "submitted",
                notesArea.getText().trim()
        );

        inserted=RepairController.addRepair(newRepair);

        if (inserted) {

        	Shared.showInformationDialog(
        	        new JFrame(),
        	        "Repair was created with success.",
        	        "Valid Form"
        	);

            clientView.fillRepairTable(false);

        } else {

            Shared.showErrorDialog(
                    new JFrame(),
                    "There was an error creating the repair.",
                    "Validation Error"
            );
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    	if (e.getSource().equals(createButton)) {
            validateFields();
        }
    }
}
