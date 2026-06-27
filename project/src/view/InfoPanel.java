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
import java.io.IOException;
import java.nio.file.Files;

/**
 * Class to manage what a client sees on the app
 */
public class InfoPanel extends JPanel implements ActionListener {
    private Integer id;
    private String type;
    private Boolean settingsTab;
    private String selectedImagePath;

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

    private JPanel imgPanel;
    private JLabel imgLabel;

    private JButton editButton;
    private JButton searchFotoButton;

    private JLabel errorLabel;

    /**
     * Function to construct the panel
     * @param id id of the account
     * @param type of the account
     * @param settingsTab Boolean to identify if the page is being displayed in the settings tab or not
     */
    public InfoPanel(Integer id, String type, Boolean settingsTab) {

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
                break;
        }

        JPanel accountPanel = new JPanel(new GridLayout(9, 1));

        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel("Name:      ", SwingConstants.RIGHT);
        nameField = new JTextField(24);
        nameField.setText(account.getName());
        nameField.setEditable(false);
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JPanel usernamePanel = new JPanel();
        JLabel usernameLabel = new JLabel("User Name: ", SwingConstants.RIGHT);
        usernameField = new JTextField(24);
        usernameField.setText(account.getUsername());
        usernameField.setEditable(false);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password:  ", SwingConstants.RIGHT);
        passwordField = new JTextField(24);
        passwordField.setText(account.getPassword());
        passwordField.setEditable(false);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        JPanel emailPanel = new JPanel();
        JLabel emailLabel = new JLabel("Email:     ", SwingConstants.RIGHT);
        emailField = new JTextField(24);
        emailField.setText(account.getEmail());
        emailField.setEditable(false);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        checkBoxPanel = new JPanel();
        JLabel activeLabel = new JLabel("Active: ", SwingConstants.RIGHT);
        activeCheckBox = new JCheckBox();
        activeCheckBox.setSelected(account.getActive());
        activeCheckBox.setEnabled(false);
        checkBoxPanel.add(activeLabel);
        checkBoxPanel.add(activeCheckBox);
        JLabel approvedLabel = new JLabel("Approved: ", SwingConstants.RIGHT);
        approvedCheckBox = new JCheckBox();
        approvedCheckBox.setSelected(account.getApproved());
        approvedCheckBox.setEnabled(false);
        checkBoxPanel.add(approvedLabel);
        checkBoxPanel.add(approvedCheckBox);

        if (settingsTab) {
            inSettingsTab(settingsTab);
        }

        JPanel typePanel = new JPanel();
        JLabel typeLabel = new JLabel("Type:      ", SwingConstants.RIGHT);
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
        JPanel personalDataPanel = new JPanel(new GridLayout(9, 1));

        JPanel nifPanel = new JPanel();
        JLabel nifLabel = new JLabel("NIF:      ", SwingConstants.RIGHT);
        nifField = new JTextField(24);
        nifField.setEditable(false);
        nifPanel.add(nifLabel);
        nifPanel.add(nifField);

        JPanel phonePanel = new JPanel();
        JLabel phoneLabel = new JLabel("Phone: ", SwingConstants.RIGHT);
        phoneField = new JTextField(24);
        phoneField.setEditable(false);
        phonePanel.add(phoneLabel);
        phonePanel.add(phoneField);

        JPanel residencePanel = new JPanel();
        JLabel residenceLabel = new JLabel("Residence:  ", SwingConstants.RIGHT);
        residenceField = new JTextField(24);
        residenceField.setEditable(false);
        residencePanel.add(residenceLabel);
        residencePanel.add(residenceField);

        personalDataPanel.add(new JPanel());
        personalDataPanel.add(nifPanel);
        personalDataPanel.add(phonePanel);
        personalDataPanel.add(residencePanel);

        Image image;
        Image scaledImage;

        switch (type) {
            case "client":
                nifField.setText(client.getNif());
                phoneField.setText(client.getPhoneNumber());
                residenceField.setText(client.getResidence());

                JPanel bussinessSectorPanel = new JPanel();
                JLabel bussinessSectorLabel = new JLabel("Business Sector: ", SwingConstants.LEFT);
                bussinessSectorField = new JTextField(24);
                bussinessSectorField.setText(client.getBusinessSector());
                bussinessSectorField.setEditable(false);
                bussinessSectorPanel.add(bussinessSectorLabel);
                bussinessSectorPanel.add(bussinessSectorField);

                JPanel tierPanel = new JPanel();
                JLabel tierLabel = new JLabel("Tier:     ", SwingConstants.RIGHT);
                tierField = new JTextField(24);
                tierField.setText(client.getTier());
                tierField.setEditable(false);
                tierPanel.add(tierLabel);
                tierPanel.add(tierField);

                personalDataPanel.add(bussinessSectorPanel);
                personalDataPanel.add(tierPanel);

                tabPanel.insertTab("Personal", null, personalDataPanel, null, 1);

                //-----Direta--
                //mudar para a img que vem da bd
                imgPanel = new JPanel();
                imgPanel.setLayout(new GridLayout(3, 1));

                imgLabel = new JLabel();
                if (client.getPic() != null && !client.getPic().isEmpty()) {
                    image = Toolkit.getDefaultToolkit().getImage(client.getPic());
                } else {
                    image = Toolkit.getDefaultToolkit().getImage("profilepics/sample.png");
                }
                scaledImage = image.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

                imgLabel.setIcon(new ImageIcon(scaledImage));

                imgPanel.add(new JPanel());
                imgPanel.add(imgLabel);
                imgPanel.repaint();
                break;

            case "employee":
                nifField.setText(employee.getNif());
                phoneField.setText(employee.getPhoneNumber());
                residenceField.setText(employee.getResidence());

                JPanel expertisePanel = new JPanel();
                JLabel expertiseLabel = new JLabel("Level Expertise: ", SwingConstants.LEFT);
                expertiseField = new JTextField(24);
                expertiseField.setText(employee.getLvlExpertise());
                expertiseField.setEditable(false);
                expertisePanel.add(expertiseLabel);
                expertisePanel.add(expertiseField);

                JPanel startDatePanel = new JPanel();
                JLabel startDateLabel = new JLabel("Start Date:     ", SwingConstants.RIGHT);
                JTextField startDateField = new JTextField(24);
                startDateField.setText(OutputController.dateFormater(employee.getStartDate(), "day"));
                startDateField.setEditable(false);
                startDatePanel.add(startDateLabel);
                startDatePanel.add(startDateField);

                personalDataPanel.add(expertisePanel);
                personalDataPanel.add(startDatePanel);

                //https://docs.oracle.com/javase/8/docs/api/javax/swing/JTabbedPane.html#insertTab-java.lang.String-javax.swing.Icon-java.awt.Component-java.lang.String-int-
                tabPanel.insertTab("Personal", null, personalDataPanel, "qwe", 1);

                //-----Direta--
                //EU SEI QUE NÃO DEVIA ESTAR A DUPLICAR CODIGO MAS ESTAMOS SEM TEMPO!!
                imgPanel = new JPanel();
                imgPanel.setLayout(new GridLayout(3, 1));

                imgLabel = new JLabel();
                if (employee.getPic() != null && !employee.getPic().isEmpty()) {
                    image = Toolkit.getDefaultToolkit().getImage(employee.getPic());
                } else {
                    image = Toolkit.getDefaultToolkit().getImage("profilepics/sample.png");
                }
                scaledImage = image.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

                imgLabel.setIcon(new ImageIcon(scaledImage));

                imgPanel.add(new JPanel());
                imgPanel.add(imgLabel);
                imgPanel.repaint();
                break;

            case null:
            default:
                //-----Direta--
                //EU SEI QUE NÃO DEVIA ESTAR A DUPLICAR CODIGO MAS ESTAMOS SEM TEMPO!!
                imgPanel = new JPanel();
                imgPanel.setLayout(new GridLayout(3, 1));

                imgLabel = new JLabel();
                if (account.getPic() != null && !account.getPic().isEmpty()) {
                    image = Toolkit.getDefaultToolkit().getImage(account.getPic());
                } else {
                    image = Toolkit.getDefaultToolkit().getImage("profilepics/sample.png");
                }
                scaledImage = image.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

                imgLabel.setIcon(new ImageIcon(scaledImage));

                imgPanel.add(new JPanel());
                imgPanel.add(imgLabel);
                imgPanel.repaint();
                break;
        }

        //------------Footer--
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

        this.add(footerPanel, BorderLayout.SOUTH);
        this.add(imgPanel, BorderLayout.EAST);
    }

    /**
     * function to change the properties of the page components
     * @param settingsTab Bool that tells if we're in settings tab
     */
    public void inSettingsTab(Boolean settingsTab) {

        if (settingsTab) {
            activeCheckBox.setEnabled(!settingsTab);
            checkBoxPanel.setVisible(!settingsTab);
            approvedCheckBox.setEnabled(!settingsTab);
            checkBoxPanel.setVisible(!settingsTab);
        } else {
            activeCheckBox.setEnabled(settingsTab);
            approvedCheckBox.setEnabled(settingsTab);
        }
    }

    /**
     * function to change the properties of the page components to editable
     * @param state Bool that indicates if the page can be editable
     */
    public void setEditable(Boolean state) {
        nameField.setEditable(state);
        usernameField.setEditable(state);
        passwordField.setEditable(state);
        emailField.setEditable(state);
        approvedCheckBox.setEnabled(state);
        activeCheckBox.setEnabled(state);
        searchFotoButton.setEnabled(state);

        if (type.equals("client")) {
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

    /**
     * Function to validate fields
     */
    public void validateFields() {
        Manager updatedManager = account;
        Employee updatedEmployee = employee;
        Client updatedClient = client;
        Boolean updated = false;

        if (nameField.getText().trim().isEmpty()) {
            Shared.showErrorDialog(new JFrame(), "Name field cannot be empty!", "Invalid Form");
            return;
        }

        if (usernameField.getText().trim().isEmpty()) {
            Shared.showErrorDialog(new JFrame(), "Username field cannot be empty!", "Invalid Form");
            return;
        }

        if (passwordField.getText().trim().isEmpty()) {
            Shared.showErrorDialog(new JFrame(), "Password field cannot be empty!", "Invalid Form");
            return;
        }

        if (!InputController.validateEmail(emailField.getText())) {
            Shared.showErrorDialog(new JFrame(), "Invalid Email", "Invalid Form");
            return;
        }

        if (!type.equals("manager")) {
            if (!InputController.validateNif(nifField.getText())) {
                Shared.showErrorDialog(new JFrame(), "Invalid NIF", "Invalid Form");
                return;
            }

            if (!InputController.validatePhone(phoneField.getText())) {
                Shared.showErrorDialog(new JFrame(), "Invalid Phone", "Invalid Form");
                return;
            }

            if (residenceField.getText().trim().isEmpty()) {
                Shared.showErrorDialog(new JFrame(), "Invalid Residence", "Invalid Form");
                return;
            }

            if (type.equals("client")) {

                if (bussinessSectorField.getText().trim().isEmpty()) {
                    Shared.showErrorDialog(new JFrame(), "Invalid Bussiness Sector", "Invalid Form");
                    return;
                }

//                if (tierField.getText().trim().isEmpty()){
//                    Shared.showErrorDialog(new JFrame(),"Invalid Tier", "Invalid Form");
//                    return;
//                }
            }

            if (type.equals("employee")) {
                if (expertiseField.getText().trim().isEmpty()) {
                    Shared.showErrorDialog(new JFrame(), "Invalid Level of Expertise", "Invalid Form");
                    return;
                }
            }
        }

        if (type.equals("manager")) {
            updatedManager.setName(nameField.getText());
            updatedManager.setUsername(usernameField.getText());
            updatedManager.setPassword(passwordField.getText());

            updatedManager.setActive(activeCheckBox.isSelected());
            updatedManager.setApproved(approvedCheckBox.isSelected());
            updatedManager.setEmail(emailField.getText());
            updatedManager.setPic(selectedImagePath);

            updated = ManagerController.updateManager(updatedManager);

        } else if (type.equals("client")) {
            updatedClient.setName(nameField.getText());
            updatedClient.setUsername(usernameField.getText());
            updatedClient.setPassword(passwordField.getText());

            updatedClient.setActive(activeCheckBox.isSelected());
            updatedClient.setApproved(approvedCheckBox.isSelected());
            updatedClient.setEmail(emailField.getText());
            updatedClient.setPic(selectedImagePath);

            updatedClient.setNif((nifField.getText()));
            updatedClient.setPhoneNumber(phoneField.getText());
            updatedClient.setResidence(residenceField.getText());

            updatedClient.setBusinessSector(bussinessSectorField.getText());
            updatedClient.setTier(tierField.getText());

            updated = ClientController.updateClient(updatedClient);

        } else if (type.equals("employee")) {
            updatedEmployee.setName(nameField.getText());
            updatedEmployee.setUsername(usernameField.getText());
            updatedEmployee.setPassword(passwordField.getText());

            updatedEmployee.setActive(activeCheckBox.isSelected());
            updatedEmployee.setApproved(approvedCheckBox.isSelected());
            updatedEmployee.setEmail(emailField.getText());
            updatedEmployee.setPic(selectedImagePath);

            updatedEmployee.setNif((nifField.getText()));
            updatedEmployee.setPhoneNumber(phoneField.getText());
            updatedEmployee.setResidence(residenceField.getText());

            updatedEmployee.setLvlExpertise(expertiseField.getText());

            updated = EmployeeController.updateEmployee(updatedEmployee);
        }


        if (updated) {
            Shared.showInformationDialog(new JFrame(), "The data was updated with success.", "Valid Form");

            editButton.setText("Edit Account");
            setEditable(false);
            beingEdited = false;
        } else {
            Shared.showErrorDialog(new JFrame(), "There was an error updating the account.", "Validation Error");
        }
    }

    /**
     * Checks when an action is performed
     * @param e used to identify which button was pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(searchFotoButton)) {
            File file;
            try {
                JFileChooser imgChooser = new JFileChooser();
                imgChooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("Image Types", "png", "jpg", "jpeg", "jfif");
                imgChooser.setFileFilter(imgFilter);
                imgChooser.showDialog(this, "Select Profile Picture");
                file = imgChooser.getSelectedFile();
                String path = file.toString();
                System.out.println(path);

                Image image = Toolkit.getDefaultToolkit().getImage(path);
                Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

                imgLabel.setIcon(new ImageIcon(scaledImage));

                imgPanel.add(new JPanel());
                imgPanel.add(imgLabel);
                imgPanel.repaint();

                //https://docs.oracle.com/javase/tutorial/essential/io/copy.html
                File src = file;
                File dest = new File("profilepics");
                File destFile = new File(dest, src.getName());
                selectedImagePath = destFile.getPath();

                Files.copy(src.toPath(), destFile.toPath());

            } catch (IOException ex) {
                Shared.showErrorDialog(new JFrame(), "There was an error loading the image (try changing it's name i think?).", "Possible Duplication");
                selectedImagePath = null;
            }
        }

        if (e.getSource().equals(editButton) && beingEdited) {
            validateFields();

        } else if (e.getSource().equals(editButton) && !beingEdited) {
            setEditable(true);
            beingEdited = true;
            editButton.setText("Save Changes");
        }

    }
}
