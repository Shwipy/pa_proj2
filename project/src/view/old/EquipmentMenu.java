//package view;
//
//import controller.EquipmentController;
//import controller.InputController;
//import model.Equipment;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * Displayed to console a menu for users to add an equipment
// */
//public class EquipmentMenu {
//
//    /**
//     * Displayed to console a menu for users to add an equipment;
//     * Apparently this functions was created but never used?
//     */
//    public static void showCreationMenu(){
//        System.out.println("Creating equipment:");
//
//        Equipment equipment = new Equipment(
//                -1,
//                InputController.readInt("Client ID> "),
//                InputController.readString("Brand> "),
//                InputController.readString("Model> "),
//                InputController.readString("SKU> "),
//                InputController.readString("Batch> "),
//                InputController.readLong("Manufacture Date> "),
//                InputController.readLong("Repair Date> "),
//                System.currentTimeMillis()/1000
//        );
//
//        if (EquipmentController.addEquipment(equipment)) {
//            System.out.println("Equipment added with success!");
//        } else {
//            System.out.println("Error creating equipment!");
//        }
//    }
//
//    public static class EquipmentWindow extends JPanel implements ActionListener {
//
//        private Integer clientId;
//        private ClientView clientView;
//
//        private JTextField brandField;
//        private JTextField modelField;
//        private JTextField batchField;
//        private JTextField manufactureDateField;
//
//        private JButton createButton;
//
//        public EquipmentWindow(Integer clientId, ClientView clientView) {
//
//            this.clientId = clientId;
//            this.clientView = clientView;
//
//            this.setLayout(new BorderLayout());
//
//            //--------------------------------------Center--------------------------------------
//            JPanel equipmentPanel = new JPanel(new GridLayout(7, 1));
//
//            JPanel titlePanel = new JPanel();
//
//            JLabel titleLabel = new JLabel(
//                    "ADD EQUIPMENT",
//                    SwingConstants.CENTER
//            );
//
//            titlePanel.add(titleLabel);
//
//            JPanel brandPanel = new JPanel();
//
//            JLabel brandLabel = new JLabel(
//                    "Brand:              ",
//                    SwingConstants.RIGHT
//            );
//
//            brandField = new JTextField(24);
//
//            brandPanel.add(brandLabel);
//            brandPanel.add(brandField);
//
//            JPanel modelPanel = new JPanel();
//
//            JLabel modelLabel = new JLabel(
//                    "Model:              ",
//                    SwingConstants.RIGHT
//            );
//
//            modelField = new JTextField(24);
//
//            modelPanel.add(modelLabel);
//            modelPanel.add(modelField);
//
//            JPanel batchPanel = new JPanel();
//
//            JLabel batchLabel = new JLabel(
//                    "Batch:              ",
//                    SwingConstants.RIGHT
//            );
//
//            batchField = new JTextField(24);
//
//            batchPanel.add(batchLabel);
//            batchPanel.add(batchField);
//
//            JPanel manufactureDatePanel = new JPanel();
//
//            JLabel manufactureDateLabel = new JLabel(
//                    "Manufacture Date: ",
//                    SwingConstants.RIGHT
//            );
//
//            manufactureDateField = new JTextField(24);
//            manufactureDateField.setToolTipText("Use the format yyyy-MM-dd.");
//
//            manufactureDatePanel.add(manufactureDateLabel);
//            manufactureDatePanel.add(manufactureDateField);
//
//            equipmentPanel.add(titlePanel);
//            equipmentPanel.add(new JPanel());
//            equipmentPanel.add(brandPanel);
//            equipmentPanel.add(modelPanel);
//            equipmentPanel.add(batchPanel);
//            equipmentPanel.add(manufactureDatePanel);
//            equipmentPanel.add(new JPanel());
//
//            //--------------------------------------Footer--------------------------------------
//            JPanel footerPanel = new JPanel();
//            footerPanel.setLayout(new GridLayout(1, 4));
//
//            createButton = new JButton("Create Equipment");
//
//            createButton.setToolTipText(
//                    "Press to validate and create a new equipment."
//            );
//
//            createButton.addActionListener(this);
//
//            footerPanel.add(new JPanel());
//            footerPanel.add(new JPanel());
//            footerPanel.add(new JPanel());
//            footerPanel.add(createButton);
//
//            //--------------------------------------Main Panel--------------------------------------
//            JScrollPane scrollPane = new JScrollPane(equipmentPanel);
//
//            this.add(scrollPane, BorderLayout.CENTER);
//            this.add(footerPanel, BorderLayout.SOUTH);
//        }
//
//        private void validateFields() {
//
//            Boolean inserted;
//            Long manufactureDate;
//
//            if (brandField.getText().trim().isEmpty()) {
//
//                Shared.showErrorDialog(
//                        new JFrame(),
//                        "Brand field cannot be empty!",
//                        "Invalid Form"
//                );
//
//                return;
//            }
//
//            if (modelField.getText().trim().isEmpty()) {
//
//                Shared.showErrorDialog(
//                        new JFrame(),
//                        "Model field cannot be empty!",
//                        "Invalid Form"
//                );
//
//                return;
//            }
//
//            if (batchField.getText().trim().isEmpty()) {
//
//                Shared.showErrorDialog(
//                        new JFrame(),
//                        "Batch field cannot be empty!",
//                        "Invalid Form"
//                );
//
//                return;
//            }
//
//            if (manufactureDateField.getText().trim().isEmpty()) {
//
//                Shared.showErrorDialog(
//                        new JFrame(),
//                        "Manufacture date field cannot be empty!",
//                        "Invalid Form"
//                );
//
//                return;
//            }
//
//            try {
//
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//                dateFormat.setLenient(false);
//
//                Date date = dateFormat.parse(manufactureDateField.getText().trim()
//                );
//
//                manufactureDate = date.getTime() / 1000;
//
//            } catch (ParseException e) {
//
//                Shared.showErrorDialog(
//                        new JFrame(),
//                        "Invalid manufacture date. Use yyyy-MM-dd.",
//                        "Invalid Form"
//                );
//
//                return;
//            }
//
//            String sku =EquipmentController.generateSKU();
//
//            if (sku == null) {
//
//                Shared.showErrorDialog(
//                        new JFrame(),
//                        "There was an error generating the SKU.",
//                        "Validation Error"
//                );
//
//                return;
//            }
//
//            Equipment newEquipment = new Equipment(
//                    -1,
//                    clientId,
//                    brandField.getText().trim(),
//                    modelField.getText().trim(),
//                    sku,
//                    batchField.getText().trim(),
//                    manufactureDate,
//                    System.currentTimeMillis() / 1000,
//                    null
//            );
//
//            inserted=EquipmentController.addEquipment(newEquipment);
//
//            if (inserted) {
//
//                Shared.showInformationDialog(
//                        new JFrame(),
//                        "Equipment was created with success.",
//                        "Valid Form"
//                );
//
//                clientView.fillEquipmentTable(false);
//
//            } else {
//
//                Shared.showErrorDialog(
//                        new JFrame(),
//                        "There was an error creating the equipment.",
//                        "Validation Error"
//                );
//            }
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//
//            if (e.getSource().equals(createButton)) {
//                validateFields();
//            }
//        }
//    }
//}
