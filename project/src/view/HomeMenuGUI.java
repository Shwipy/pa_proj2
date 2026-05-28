package view;
import controller.ManagerController;
import controller.OutputController;
import controller.SessionController;

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
        setTitle(accountType.toUpperCase());
        setSize(780, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new GridLayout(1, 5));

        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.add(new JLabel(ManagerController.getManager(userId).getName(), SwingConstants.LEFT));

        JPanel DatePanel = new JPanel(new FlowLayout());
        Long now = System.currentTimeMillis()/1000;
        DatePanel.add(new JLabel(OutputController.dateFormater(now,"day"), SwingConstants.LEFT));

        logoutButton = new JButton("Logout");
        logoutButton.setToolTipText("Return to login.");
        logoutButton.addActionListener(this);

        footerPanel.add(namePanel);
        footerPanel.add(DatePanel);
        footerPanel.add(new JPanel());
        footerPanel.add(new JPanel());
        footerPanel.add(logoutButton);

        if (accountType.equals("manager")){
            cont.add( new ManagerView().show(), BorderLayout.CENTER);
        } else if (accountType.equals("employee")) {
        	cont.add(new EmployeeView().show(), BorderLayout.CENTER);
        } else if (accountType.equals("client")) {
        	cont.add( new ClientView().show(), BorderLayout.CENTER);
        }

        cont.add(footerPanel,BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(logoutButton)) {

            if(Shared.showDialog(this,"Are you sure you want to logout?", "Logout")){
                LoginGUI login = new LoginGUI();
                login.init();
                login.setVisible(true);

                this.dispose();
            }
        }

    }
}
