package view;

import javax.swing.*;
import java.awt.*;

public class InfoDialog extends JPanel{
    Integer id;
    String type;

    public InfoDialog(Integer id, String type){
        this.id = id;
        this.type = type;
        this.setLayout(new FlowLayout());

        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField();
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        this.add(namePanel);
        //this.add(new JLabel("nice"+id),BorderLayout.NORTH);
        System.out.println("ramen");
    }


}
