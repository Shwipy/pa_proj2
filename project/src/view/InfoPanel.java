package view;

import controller.*;
import model.Client;
import model.Employee;
import model.Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class InfoPanel extends JPanel implements ActionListener {
    private Integer id;
    private String type;

    private Client client = null;
    private Employee employee = null;
    private Manager account = null;

    private Boolean beingEdited = false;
    private Boolean valid = true;

    private JTextField nameField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField emailField;
    private JCheckBox activeCheckBox;
    private JCheckBox approvedCheckBox;
    private JTextField typeField;
    private JTextField nifField;
    private JTextField phoneField;
    private JTextField residenceField;
    private JTextField expertiseField;

    private JButton editButton;
    private JButton banButton;
    private JButton approveButton;

    private JLabel errorLabel;

    public InfoPanel(Integer id, String type){
        this.id = id;
        this.type = type;

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

        JPanel checkBoxPanel = new JPanel();
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
                System.out.println("User id not found!");
                break;
        }

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new GridLayout(1, 5));


        editButton = new JButton("Edit Account");
        editButton.addActionListener(this);

        errorLabel = new JLabel("errormessage test");
        errorLabel.setVisible(false);

        banButton = new JButton("Ban");
        banButton.addActionListener(this);

        approveButton = new JButton("Approve");
        approveButton.addActionListener(this);

        footerPanel.add(errorLabel);
        footerPanel.add(banButton);
        footerPanel.add(new JPanel());
        footerPanel.add(editButton);

        this.add(footerPanel,BorderLayout.SOUTH);
    }
    public void setEditable(Boolean state){
        nameField.setEditable(state);
        usernameField.setEditable(state);
        passwordField.setEditable(state);
        emailField.setEditable(state);

        //typeField.setEditable(true);
        //nifField.setEditable(true);
        //phoneField.setEditable(true);
        //residenceField.setEditable(true);
        //expertiseField.setEditable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(editButton) && editButton.getText().equals("Edit Account")){

            setEditable(true);
            beingEdited = true;
            editButton.setText("Save Changes");
        }
        if (e.getSource().equals(editButton) && editButton.getText().equals("Save Changes")) {
            beingEdited = false;
            valid=true;
            switch (type) {
//                case "client":
//                    Client updatedClient = client;
//                    ManagerController.updateManager(updatedManager);
//                    break;
//
//                case "employee":
//                    employee = EmployeeController.getEmployee(id);
//                    account = ManagerController.getManager(id);
//                    break;

                case "manager":

                    Manager updatedManager = account;
                    updatedManager.setName(nameField.getText());
                    updatedManager.setUsername(usernameField.getText());
                    updatedManager.setPassword(passwordField.getText());

                    if(InputController.validateEmail(emailField.getText())){
                        updatedManager.setEmail(emailField.getText());
                    }else{
                        valid=false;
                    }
                    if (valid){
                        Boolean updated = ManagerController.updateManager(updatedManager);

                        if (updated){
                            errorLabel.setText("Updated with Success");
                            errorLabel.setForeground(Color.GREEN);
                            errorLabel.setVisible(true);

                            setEditable(false);
                            beingEdited = false;
                            editButton.setText("Edit Account");
                        }else{
                            errorLabel.setText("Invalid Data");
                            errorLabel.setForeground(Color.RED);
                            errorLabel.setVisible(true);
                        }

                    }else{
                        errorLabel.setText("Invalid Data");
                        errorLabel.setForeground(Color.RED);
                        errorLabel.setVisible(true);
                    }

                    break;

                case null:
                default:
                    System.out.println("User id not found!");
                    break;
            }
        }

        if (e.getSource().equals(banButton)) {
            ManagerController.banAccount(id, "ban");

        }

        if (e.getSource().equals(approveButton) ) {

        }
    }
}
