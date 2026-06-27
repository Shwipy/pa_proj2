package view;

import controller.*;
import model.Client;
import model.Employee;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
/**
 * Register Account Graphical Interface, allow the user to create an account or to navigate back to login
 */
public class RegisterGUI extends JFrame implements ActionListener {
    private Boolean accountTypeClient = true;
    private String selectedImagePath = "sample.png";

    private JPanel accountPanel;
    private JPanel expertisePanel;
    //private JPanel startDatePanel;
    private JPanel bussinessSectorPanel;
    private JPanel tierPanel;
    private JPanel imgPanel;

    private JLabel imgLabel;

    private JTextField nameField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField emailField;
    private JTextField typeField;
    private JTextField nifField;
    private JTextField phoneField;
    private JTextField residenceField;
    private JTextField expertiseField;
    private JTextField bussinessSectorField;
    private JTextField tierField;

    private JButton loginButton;
    private JButton searchFotoButton;
    private JButton createButton;
    private JButton switchTypeButton;

    /**
     * Initializes the JFrame and all components in it
     */
    public void init() {
        setTitle("Register Form");
        setSize(780, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());
        //--------------------------------------Footer--------------------------------------
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new GridLayout(1, 5));

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        searchFotoButton = new JButton("Select Img");
        searchFotoButton.addActionListener(this);

        createButton = new JButton("Create Account");
        createButton.setToolTipText("Press to validate and create a new account.");
        createButton.addActionListener(this);

        footerPanel.add(loginButton);
        footerPanel.add(new JPanel());
        footerPanel.add(new JPanel());
        footerPanel.add(searchFotoButton);
        footerPanel.add(createButton);
        //--------------------------------------Right--------------------------------------
        imgPanel = new JPanel();
        imgPanel.setLayout(new GridLayout(3, 1));

        imgLabel = new JLabel();

        Image image = Toolkit.getDefaultToolkit().getImage("profilepics/sample.png");
        Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

        imgLabel.setIcon( new ImageIcon(scaledImage) );

        imgPanel.add(new JPanel());
        imgPanel.add(imgLabel);
        imgPanel.repaint();
       //https://docs.oracle.com/javase/tutorial/essential/io/copy.html
        //--------------------------------------Center--------------------------------------
        accountPanel = new JPanel(new GridLayout(12, 1));

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("REGISTER FORM:",SwingConstants.CENTER);
        titlePanel.add(titleLabel);

        JPanel typePanel = new JPanel();
        JLabel typeLabel = new JLabel("Type:      ",SwingConstants.RIGHT);
        typeField = new JTextField(24);
        typeField.setText("Client");
        typeField.setEnabled(false);
        switchTypeButton = new JButton("Switch");
        switchTypeButton.addActionListener(this);
        typePanel.add(typeLabel);
        typePanel.add(typeField);
        typePanel.add(switchTypeButton);

        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel("Name:      ",SwingConstants.RIGHT);
        nameField = new JTextField(24);
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JPanel usernamePanel = new JPanel();
        JLabel usernameLabel = new JLabel("User Name: ",SwingConstants.RIGHT);
        usernameField = new JTextField(24);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password:  ",SwingConstants.RIGHT);
        passwordField = new JPasswordField(24);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        JPanel emailPanel = new JPanel();
        JLabel emailLabel = new JLabel("Email:     ",SwingConstants.RIGHT);
        emailField = new JTextField(24);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        accountPanel.add(titlePanel);
        accountPanel.add(typePanel);
        accountPanel.add(namePanel);
        accountPanel.add(usernamePanel);
        accountPanel.add(passwordPanel);
        accountPanel.add(emailPanel);

        JPanel nifPanel = new JPanel();
        JLabel nifLabel = new JLabel("NIF:      ",SwingConstants.RIGHT);
        nifField = new JTextField(24);
        nifPanel.add(nifLabel);
        nifPanel.add(nifField);

        JPanel phonePanel = new JPanel();
        JLabel phoneLabel = new JLabel("Phone: ",SwingConstants.RIGHT);
        phoneField = new JTextField(24);
        phonePanel.add(phoneLabel);
        phonePanel.add(phoneField);

        JPanel residencePanel = new JPanel();
        JLabel residenceLabel = new JLabel("Residence:  ",SwingConstants.RIGHT);
        residenceField = new JTextField(24);
        residencePanel.add(residenceLabel);
        residencePanel.add(residenceField);

        accountPanel.add(new JPanel());
        accountPanel.add(nifPanel);
        accountPanel.add(phonePanel);
        accountPanel.add(residencePanel);

        bussinessSectorPanel = new JPanel();
        JLabel bussinessSectorLabel = new JLabel("Business Sector: ",SwingConstants.LEFT);
        bussinessSectorField = new JTextField(24);
        bussinessSectorPanel.add(bussinessSectorLabel);
        bussinessSectorPanel.add(bussinessSectorField);

        tierPanel = new JPanel();
        JLabel tierLabel = new JLabel("Tier:     ",SwingConstants.RIGHT);
        tierField = new JTextField(24);
        tierPanel.add(tierLabel);
        tierPanel.add(tierField);

        accountPanel.add(bussinessSectorPanel);
        accountPanel.add(tierPanel);

        expertisePanel = new JPanel();
        JLabel expertiseLabel = new JLabel("Level Expertise: ",SwingConstants.LEFT);
        expertiseField = new JTextField(24);
        expertisePanel.add(expertiseLabel);
        expertisePanel.add(expertiseField);

        //startDatePanel = new JPanel();
        //JLabel startDateLabel = new JLabel("Start Date:     ",SwingConstants.RIGHT);
        //JTextField startDateField = new JTextField(24);
        //startDatePanel.add(startDateLabel);
        //startDatePanel.add(startDateField);

        JScrollPane scrollPane = new JScrollPane(accountPanel);
        cont.add(scrollPane, BorderLayout.CENTER);
        cont.add(imgPanel, BorderLayout.EAST);
        cont.add(footerPanel, BorderLayout.SOUTH);
    }

    private void userRegister(){
        Boolean inserted;

        if (nameField.getText().trim().isEmpty()) {
            Shared.showErrorDialog(new JFrame(),"Name field cannot be empty!", "Invalid Form");
            return;
        }

        if (usernameField.getText().trim().isEmpty()) {
            Shared.showErrorDialog(new JFrame(),"Username field cannot be empty!", "Invalid Form");
            return;
        }

        if (passwordField.getText().trim().isEmpty()) {
            Shared.showErrorDialog(new JFrame(),"Password field cannot be empty!", "Invalid Form");
            return;
        }

        if(!InputController.validateEmail(emailField.getText())){
            Shared.showErrorDialog(new JFrame(),"Invalid Email", "Invalid Form");
            return;
        }

        if (!InputController.validateNif(nifField.getText())){
            Shared.showErrorDialog(new JFrame(),"Invalid NIF", "Invalid Form");
            return;
        }

        if (!InputController.validatePhone(phoneField.getText())){
            Shared.showErrorDialog(new JFrame(),"Invalid Phone", "Invalid Form");
            return;
        }

        if (residenceField.getText().trim().isEmpty()){
            Shared.showErrorDialog(new JFrame(),"Invalid Residence", "Invalid Form");
            return;
        }

        if (accountTypeClient){

            if (bussinessSectorField.getText().trim().isEmpty()){
                Shared.showErrorDialog(new JFrame(),"Invalid Bussiness Sector", "Invalid Form");
                return;
            }

//            if (tierField.getText().trim().isEmpty()){
//                Shared.showErrorDialog(new JFrame(),"Invalid Tier", "Invalid Form");
//                return;
//            }
        } else {
            if (expertiseField.getText().trim().isEmpty()){
                Shared.showErrorDialog(new JFrame(),"Invalid Level of Expertise", "Invalid Form");
                return;
            }
        }

        if (accountTypeClient) {
            Client newClient = new Client(
                    -1,
                    nameField.getText(),
                    usernameField.getText().toLowerCase(),
                    passwordField.getText(),
                true,
                false,
                    emailField.getText().toLowerCase(),
                    selectedImagePath,
                "client",
                    nifField.getText(),
                    phoneField.getText(),
                    residenceField.getText(),
                bussinessSectorField.getText(),
                ""
        );
            System.out.println(selectedImagePath);

            inserted = ClientController.addClient(newClient);

        } else {

            Employee newEmployee = new Employee(
                    -1,
                    nameField.getText(),
                    usernameField.getText().toLowerCase(),
                    passwordField.getText(),
                    true,
                    false,
                    emailField.getText().toLowerCase(),
                    selectedImagePath,
                    "employee",
                    nifField.getText(),
                    phoneField.getText(),
                    residenceField.getText(),
                    expertiseField.getText(),
                    System.currentTimeMillis() / 1000
            );

            inserted = EmployeeController.addEmployee(newEmployee);
        }

        if (inserted){
            Shared.showInformationDialog(new JFrame(),"User was created with success. Wait for the account to get validated.","Valid Form");
        }else{
            Shared.showErrorDialog(new JFrame(),"There was an error creating the account (duplicate field).","Validation Error");
        }
    }

    /**
     * Checks when an action is performed
     * @param e used to identify which button was pressed
     */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(loginButton)) {
            LoginGUI login = new LoginGUI();
            login.init();
            login.setVisible(true);
            this.dispose();
        }

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

        if (e.getSource().equals(switchTypeButton)) {
            if (accountTypeClient){

                accountTypeClient=false;
                typeField.setText("Employee");

                accountPanel.remove(bussinessSectorPanel);
                accountPanel.remove(tierPanel);

                accountPanel.add(expertisePanel);
                //accountPanel.add(startDatePanel);

                accountPanel.revalidate();
                accountPanel.repaint();
            }else{

                accountTypeClient=true;
                typeField.setText("Client");

                accountPanel.remove(expertisePanel);
                //accountPanel.remove(startDatePanel);

                accountPanel.add(bussinessSectorPanel);
                accountPanel.add(tierPanel);

                accountPanel.revalidate();
                accountPanel.repaint();
            }
        }

        if (e.getSource().equals(createButton)) {
            userRegister();
        }
    }

}
