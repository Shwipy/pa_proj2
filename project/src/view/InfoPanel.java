package view;

import controller.*;
import model.Client;
import model.Employee;
import model.Manager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class InfoPanel extends JPanel implements ActionListener {
    private Integer id;
    private String type;
    private Boolean settingsTab;

    private Client client = null;
    private Employee employee = null;
    private Manager account = null;

    private Boolean beingEdited = false;
    private Boolean valid = true;

    private JTextField nameField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField emailField;
    JPanel checkBoxPanel;
    private JCheckBox activeCheckBox;
    private JCheckBox approvedCheckBox;
    private JTextField typeField;
    private JTextField nifField;
    private JTextField phoneField;
    private JTextField residenceField;
    private JTextField expertiseField;
    private JTextField bussinessSectorField;
    private JTextField tierField;

    private JButton editButton;
    private JButton searchFotoButton;

    private JLabel errorLabel;

    public InfoPanel(Integer id, String type, Boolean settingsTab){

        this.id = id;
        this.type = type;
        this.settingsTab = settingsTab;

        this.setLayout(new BorderLayout());

        JTabbedPane tabPanel = new JTabbedPane();
        this.add(tabPanel, BorderLayout.CENTER);

        switch (type) {
            case "client":
                client = ClientController.getClient((id));
                account = ManagerController.getManager(id);
                break;

            case "employee":
                employee = EmployeeController.getEmployee(id);
                account = ManagerController.getManager(id);
                break;

            case "manager":
                account = ManagerController.getManager(id);
                break;

            case null:
            default:
                System.out.println("User id not found!");
                break;
        }

        JPanel accountPanel = new JPanel(new GridLayout(9,1));

        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel("Name:      ",SwingConstants.RIGHT);
        nameField = new JTextField(24);
        nameField.setText(account.getName());
        nameField.setEditable(false);
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JPanel usernamePanel = new JPanel();
        JLabel usernameLabel = new JLabel("User Name: ",SwingConstants.RIGHT);
        usernameField = new JTextField(24);
        usernameField.setText(account.getUsername());
        usernameField.setEditable(false);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password:  ",SwingConstants.RIGHT);
        passwordField = new JTextField(24);
        passwordField.setText(account.getPassword());
        passwordField.setEditable(false);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        JPanel emailPanel = new JPanel();
        JLabel emailLabel = new JLabel("Email:     ",SwingConstants.RIGHT);
        emailField = new JTextField(24);
        emailField.setText(account.getEmail());
        emailField.setEditable(false);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        checkBoxPanel = new JPanel();
        JLabel activeLabel = new JLabel("Active: ",SwingConstants.RIGHT);
        activeCheckBox = new JCheckBox();
        activeCheckBox.setSelected(account.getActive());
        activeCheckBox.setEnabled(false);
        checkBoxPanel.add(activeLabel);
        checkBoxPanel.add(activeCheckBox);
        JLabel approvedLabel = new JLabel("Approved: ",SwingConstants.RIGHT);
        approvedCheckBox = new JCheckBox();
        approvedCheckBox.setSelected(account.getApproved());
        approvedCheckBox.setEnabled(false);
        checkBoxPanel.add(approvedLabel);
        checkBoxPanel.add(approvedCheckBox);

        if (settingsTab){
            inSettingsTab(settingsTab);
        }

        JPanel typePanel = new JPanel();
        JLabel typeLabel = new JLabel("Type:      ",SwingConstants.RIGHT);
        typeField = new JTextField(24);
        typeField.setText(account.getType());
        typeField.setEditable(false);
        typePanel.add(typeLabel);
        typePanel.add(typeField);

        accountPanel.add(new JPanel());
        accountPanel.add(namePanel);
        accountPanel.add(usernamePanel);
        accountPanel.add(passwordPanel);
        accountPanel.add(emailPanel);
        accountPanel.add(checkBoxPanel);
        accountPanel.add(typePanel);

        tabPanel.addTab("Account", accountPanel);

        //-------------------------------------------------------Client + Employee Fields
        JPanel personalDataPanel = new JPanel(new GridLayout(9,1));

        JPanel nifPanel = new JPanel();
        JLabel nifLabel = new JLabel("NIF:      ",SwingConstants.RIGHT);
        nifField = new JTextField(24);
        nifField.setEditable(false);
        nifPanel.add(nifLabel);
        nifPanel.add(nifField);

        JPanel phonePanel = new JPanel();
        JLabel phoneLabel = new JLabel("Phone: ",SwingConstants.RIGHT);
        phoneField = new JTextField(24);
        phoneField.setEditable(false);
        phonePanel.add(phoneLabel);
        phonePanel.add(phoneField);

        JPanel residencePanel = new JPanel();
        JLabel residenceLabel = new JLabel("Residence:  ",SwingConstants.RIGHT);
        residenceField = new JTextField(24);
        residenceField.setEditable(false);
        residencePanel.add(residenceLabel);
        residencePanel.add(residenceField);

        personalDataPanel.add(new JPanel());
        personalDataPanel.add(nifPanel);
        personalDataPanel.add(phonePanel);
        personalDataPanel.add(residencePanel);

        switch (type) {
            case "client":
                nifField.setText(client.getNif());
                phoneField.setText(client.getPhoneNumber());
                residenceField.setText(client.getResidence());

                JPanel bussinessSectorPanel = new JPanel();
                JLabel bussinessSectorLabel = new JLabel("Business Sector: ",SwingConstants.LEFT);
                bussinessSectorField = new JTextField(24);
                bussinessSectorField.setText(client.getBusinessSector());
                bussinessSectorField.setEditable(false);
                bussinessSectorPanel.add(bussinessSectorLabel);
                bussinessSectorPanel.add(bussinessSectorField);

                JPanel tierPanel = new JPanel();
                JLabel tierLabel = new JLabel("Tier:     ",SwingConstants.RIGHT);
                tierField = new JTextField(24);
                tierField.setText(client.getTier());
                tierField.setEditable(false);
                tierPanel.add(tierLabel);
                tierPanel.add(tierField);

                personalDataPanel.add(bussinessSectorPanel);
                personalDataPanel.add(tierPanel);

                tabPanel.insertTab("Personal",null, personalDataPanel,null,1);
                break;

            case "employee":
                nifField.setText(employee.getNif());
                phoneField.setText(employee.getPhoneNumber());
                residenceField.setText(employee.getResidence());

                JPanel expertisePanel = new JPanel();
                JLabel expertiseLabel = new JLabel("Level Expertise: ",SwingConstants.LEFT);
                expertiseField = new JTextField(24);
                expertiseField.setText(employee.getLvlExpertise());
                expertiseField.setEditable(false);
                expertisePanel.add(expertiseLabel);
                expertisePanel.add(expertiseField);

                JPanel startDatePanel = new JPanel();
                JLabel startDateLabel = new JLabel("Start Date:     ",SwingConstants.RIGHT);
                JTextField startDateField = new JTextField(24);
                startDateField.setText(OutputController.dateFormater(employee.getStartDate(), "day"));
                startDateField.setEditable(false);
                startDatePanel.add(startDateLabel);
                startDatePanel.add(startDateField);

                personalDataPanel.add(expertisePanel);
                personalDataPanel.add(startDatePanel);

                //https://docs.oracle.com/javase/8/docs/api/javax/swing/JTabbedPane.html#insertTab-java.lang.String-javax.swing.Icon-java.awt.Component-java.lang.String-int-
                tabPanel.insertTab("Personal",null, personalDataPanel,"qwe",1);
                break;

            case null:
            default:
                break;
        }

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new GridLayout(1, 5));


        editButton = new JButton("Edit Account");
        editButton.addActionListener(this);

        searchFotoButton = new JButton("Select Img");
        searchFotoButton.addActionListener(this);
        searchFotoButton.setEnabled(false);

        errorLabel = new JLabel("errormessage test");
        errorLabel.setVisible(false);

        footerPanel.add(errorLabel);
        footerPanel.add(new JPanel());
        footerPanel.add(new JPanel());
        footerPanel.add(searchFotoButton);
        footerPanel.add(editButton);

        this.add(footerPanel,BorderLayout.SOUTH);
    }

    public void inSettingsTab(Boolean settingsTab){

        if (settingsTab){
            activeCheckBox.setEnabled(!settingsTab);
            checkBoxPanel.setVisible(!settingsTab);
            approvedCheckBox.setEnabled(!settingsTab);
            checkBoxPanel.setVisible(!settingsTab);
        }else{
            activeCheckBox.setEnabled(settingsTab);
            approvedCheckBox.setEnabled(settingsTab);
        }
    }

    public void setEditable(Boolean state){
        nameField.setEditable(state);
        usernameField.setEditable(state);
        passwordField.setEditable(state);
        emailField.setEditable(state);
        approvedCheckBox.setEnabled(state);
        activeCheckBox.setEnabled(state);
        searchFotoButton.setEnabled(state);

        if (type.equals("client")){
            nifField.setEditable(true);
            phoneField.setEditable(true);
            residenceField.setEditable(true);

        } else if (type.equals("employee")) {
            nifField.setEditable(true);
            phoneField.setEditable(true);
            residenceField.setEditable(true);
            expertiseField.setEditable(true);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(searchFotoButton)) {
            JFileChooser imgChooser = new JFileChooser();
            imgChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("Image Types","png", "jpg", "jpeg");
            imgChooser.setFileFilter(imgFilter);
            imgChooser.showDialog(this, "OPEN");
            File file = imgChooser.getSelectedFile ();
            System.out.println(file.toString());
        }

        if (e.getSource().equals(editButton) && beingEdited) {
            valid = true;
            switch (type) {
//                case "client":
//                    Client updatedClient = client;
//                    ManagerController.updateManager(updatedManager);
//                    break;
//
                case "employee":
                    employee = EmployeeController.getEmployee(id);
                    account = ManagerController.getManager(id);


                    break;

                case "manager":
                    Manager updatedManager = account;

                    if (nameField.getText().trim().isEmpty()) {
                        Shared.showErrorDialog(new JFrame(),"Name field cannot be empty!", "Invalid Form");
                        return;
                    }
                    updatedManager.setName(nameField.getText());

                    if (usernameField.getText().trim().isEmpty()) {
                        Shared.showErrorDialog(new JFrame(),"Username field cannot be empty!", "Invalid Form");
                        return;
                    }
                    updatedManager.setUsername(usernameField.getText());

                    if (passwordField.getText().trim().isEmpty()) {
                        Shared.showErrorDialog(new JFrame(),"Password field cannot be empty!", "Invalid Form");
                        return;
                    }
                    updatedManager.setPassword(passwordField.getText());

                    updatedManager.setActive(activeCheckBox.isSelected());
                    updatedManager.setApproved(approvedCheckBox.isSelected());

                    if(!InputController.validateEmail(emailField.getText())){
                        Shared.showErrorDialog(new JFrame(),"Invalid Email", "Invalid Form");
                        return;
                    }
                    updatedManager.setEmail(emailField.getText());

                    Boolean updated = ManagerController.updateManager(updatedManager);

                    if (updated){
                        Shared.showInformationDialog(new JFrame(),"The data was updated with success.","Valid Form");

                        editButton.setText("Edit Account");
                        setEditable(false);
                        beingEdited = false;
                    }else{
                        Shared.showErrorDialog(new JFrame(),"There was an error updating the account.","Validation Error");
                    }

                    break;

                case null:
                default:
                    break;
            }
        } else if (e.getSource().equals(editButton) && !beingEdited) {
            setEditable(true);
            beingEdited = true;
            editButton.setText("Save Changes");
        }

    }
}
