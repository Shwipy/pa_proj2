package view;

import controller.InputController;
import controller.SessionController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuGUI extends JFrame implements ActionListener{

    private Container cont;
    JButton createButton, loginButton;
    JTextField userField, passwordField;
    Integer userId;

    public void init(){
        cont = getContentPane();
        cont.setLayout (new BorderLayout());
        setSize(480, 240);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(7,1));

        JPanel usernamePanel = new JPanel(new FlowLayout());
        usernamePanel.add(new JLabel("Username:", SwingConstants.RIGHT));
         userField = new JTextField(24);
        usernamePanel.add(userField);

        JPanel passwordPanel = new JPanel(new FlowLayout());
        passwordPanel.add(new JLabel("Password:", SwingConstants.RIGHT));
         passwordField = new JTextField(24);
        passwordPanel.add(passwordField);


        JLabel titleLabel = new JLabel("Login Form", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        centerPanel.add(titleLabel);
        centerPanel.add(new JPanel());

        centerPanel.add(usernamePanel);
        centerPanel.add(new JPanel());
        centerPanel.add(passwordPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        createButton = new JButton("    Create    ");
        createButton.addActionListener(this);
        buttonPanel.add (createButton);

        buttonPanel.add(new JPanel());

        loginButton = new JButton("    Login     ");
        loginButton.addActionListener(this);
        buttonPanel.add (loginButton);


        cont.add(centerPanel,BorderLayout.CENTER);
        cont.add(buttonPanel, BorderLayout.SOUTH);


    }

    public void actionPerformed (ActionEvent e){
        if (e.getSource().equals(createButton)){
            System.out.println("register");
        }

        else {
            userId = SessionController.login(userField.getText(), passwordField.getText());
            System.out.println(userId);
        }
    }



}
