import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Locale;


public class Home extends JPanel {


    Home (){


        this.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel title = new JLabel("WELCOME TO LIBRARY");
        Font font = new Font("Verdana", Font.BOLD, 24);
        title.setFont(font);
        headerPanel.add(title);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        DefaultListModel<String> model = new DefaultListModel<>();
        JList custList = new JList(model);
        JScrollPane sc = new JScrollPane(custList);
        leftPanel.add(sc);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel customerPanel = new JPanel();
        JLabel customerLabel = new JLabel("Customer's name");
        JTextField customerText = new JTextField(20);
        JButton customerButton = new JButton("Search");
        customerPanel.add(customerLabel); customerPanel.add(customerText); customerPanel.add(customerButton);

        JPanel bookPanel = new JPanel();
        JLabel bookLabel = new JLabel("Book's name");
        JTextField bookText = new JTextField(20);
        JButton bookButton = new JButton("Search");
        bookPanel.add(bookLabel); bookPanel.add(bookText); bookPanel.add(bookButton);

        mainPanel.add(customerPanel); mainPanel.add(bookPanel);

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout());
        JButton exitButton = new JButton("EXIT");
        JButton lendButton = new JButton("LEND");
        JButton returnButton = new JButton("RETURN");
        footerPanel.add(lendButton);footerPanel.add(returnButton);footerPanel.add(exitButton);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(footerPanel, BorderLayout.SOUTH);
        this.add(leftPanel, BorderLayout.WEST);


        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from library.books");

                    model.clear();
                    while (resultSet.next()) {

                        String code = resultSet.getString("CODE");
                        String title = resultSet.getString("TITLE");
                        String state = resultSet.getString("STATE");

                        if (title.toLowerCase(Locale.ROOT).contains(bookText.getText().toLowerCase(Locale.ROOT)) | code.contains(bookText.getText().toLowerCase(Locale.ROOT))){
                            model.addElement(code + " | " + title + " | " + state);
                        }

                    }
                    if(model.isEmpty()){
                        model.addElement("Τhere is no book with this information");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });


        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from library.customers");

                    model.clear();
                    while (resultSet.next()) {

                        String code = resultSet.getString("CODE");
                        String name = resultSet.getString("NAME");
                        String surName = resultSet.getString("SURNAME");
                        String email = resultSet.getString("EMAIL");
                        String phone = resultSet.getString("PHONE");
                        String state = resultSet.getString("STATE");


                        if (name.toLowerCase(Locale.ROOT).contains(customerText.getText().toLowerCase(Locale.ROOT)) |
                                surName.toLowerCase(Locale.ROOT).contains(customerText.getText().toLowerCase(Locale.ROOT)) |
                                email.toLowerCase(Locale.ROOT).contains(customerText.getText().toLowerCase(Locale.ROOT)) |
                                phone.toLowerCase(Locale.ROOT).contains(customerText.getText().toLowerCase(Locale.ROOT)) |
                                code.toLowerCase(Locale.ROOT).contains(customerText.getText().toLowerCase(Locale.ROOT))){

                            model.addElement(code + " | "+ name + " | " + surName + " | " + email + " | " + phone + " | " + state);

                        }
                    }

                    if(model.isEmpty()){
                        model.addElement("Τhere is no customer with this information");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }
}
