package view;
import controller.SessionController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Login Graphical Interface, allow the user to authenticate himself or to navigate to create an account
 */
public class LoginGUI extends JFrame implements ActionListener {

    private Container cont;

    private JButton createButton;
    private JButton loginButton;

    private JTextField userField;
    private JPasswordField passwordField;

    private JPanel loginErrorPanel;
    private JLabel loginErrorLabel;

    private Integer userId;

    /**
     * Initializes the JFrame and selects what to put in the center panel, based on the type of account
     */
    public void init() {
        cont = getContentPane();
        cont.setLayout(new BorderLayout());

        setTitle("Login Form");
        setResizable(false);
        setSize(480, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(7, 1));

        JLabel titleLabel = new JLabel("Login Form", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel usernamePanel = new JPanel(new FlowLayout());
        usernamePanel.add(new JLabel("Username:", SwingConstants.RIGHT));

        userField = new JTextField(24);
        userField.setToolTipText("Enter your username.");
        userField.setText("manager"); //------------------------------------------------------------REMOVE
        usernamePanel.add(userField);

        JPanel passwordPanel = new JPanel(new FlowLayout());
        passwordPanel.add(new JLabel("Password:", SwingConstants.RIGHT));

        passwordField = new JPasswordField(24);
        passwordField.setToolTipText("Enter your password.");
        passwordField.setText("123"); //------------------------------------------------------------REMOVE
        passwordPanel.add(passwordField);

        loginErrorPanel = new JPanel(new FlowLayout());

        loginErrorLabel = new JLabel("Invalid credentials, pending account or inactive account.");
        loginErrorLabel.setForeground(Color.RED);

        loginErrorPanel.add(loginErrorLabel);
        loginErrorPanel.setVisible(false);

        centerPanel.add(titleLabel);
        centerPanel.add(new JPanel());
        centerPanel.add(usernamePanel);
        centerPanel.add(new JPanel());
        centerPanel.add(passwordPanel);
        centerPanel.add(loginErrorPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
  
        createButton = new JButton("    Create    ");
        createButton.setToolTipText("Create a new account.");
        createButton.addActionListener(this);
        buttonPanel.add(createButton);

        buttonPanel.add(new JPanel());

        loginButton = new JButton("     Login    ");
        loginButton.setToolTipText("Login into the application.");
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);

        cont.add(centerPanel, BorderLayout.CENTER);
        cont.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Checks when an action is performed
     * @param e used to identify which button was pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(createButton)) {
            RegisterGUI register = new RegisterGUI();
            register.init();
            register.setVisible(true);
            this.dispose();
        }

        if (e.getSource().equals(loginButton)) {
            doLogin();
        }
    }

    private void doLogin() {
        String username = userField.getText();
        String password = new String(passwordField.getPassword());

        loginErrorPanel.setVisible(false);

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            loginErrorLabel.setText("Please fill in username and password.");
            loginErrorPanel.setVisible(true);
            return;
        }

        userId = SessionController.login(username, password);

        if (userId == null) {
            loginErrorLabel.setText("Invalid credentials, pending account or inactive account.");
            loginErrorPanel.setVisible(true);
        } else {

            String accountType = SessionController.getAccountType(userId);

            System.out.println("User ID: " + userId);
            System.out.println("Account type: " + accountType);

            HomeMenuGUI home = new HomeMenuGUI(userId,accountType);
            home.init();
            home.setVisible(true);
            this.dispose();
        }
    }
}
