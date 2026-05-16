package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeMenuGUI extends JFrame implements ActionListener {

    private Integer userId;
    private String accountType;

    private JButton logoutButton;

    public HomeMenuGUI(Integer userId, String accountType) {
        this.userId = userId;
        this.accountType = accountType;
    }

    public void init() {
        setTitle("Main Menu");
        setSize(500, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel centerPanel = new JPanel(new GridLayout(4, 1));

        JLabel userIdLabel = new JLabel("User ID: " + userId, SwingConstants.CENTER);
        JLabel typeLabel = new JLabel("Account Type: " + accountType, SwingConstants.CENTER);

        JLabel infoLabel = new JLabel("This menu will be developed in the next steps.", SwingConstants.CENTER);

        centerPanel.add(new JPanel());
        centerPanel.add(userIdLabel);
        centerPanel.add(typeLabel);
        centerPanel.add(infoLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());

       
        logoutButton = new JButton("Logout");
        logoutButton.setToolTipText("Return to login.");
        logoutButton.addActionListener(this);

        buttonPanel.add(logoutButton);

        cont.add(titleLabel, BorderLayout.NORTH);
        cont.add(centerPanel, BorderLayout.CENTER);
        cont.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(logoutButton)) {
            MainMenuGUI login = new MainMenuGUI();
            login.init();
            login.setVisible(true);

            this.dispose();
        }
    }
}
