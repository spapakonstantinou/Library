import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Customers extends JPanel {

    static DefaultTableModel tableModel;
    JTable table;

    JTextField codeText;
    JTextField nameText;
    JTextField surNameText;
    JTextField emailText;
    JTextField phoneText;
    JTextField stateText;

    Customers() {


        this.setLayout(new BorderLayout());

        //Panel for Title
        JPanel headerPanel = new JPanel();/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        headerPanel.setLayout(new FlowLayout());
        JLabel title = new JLabel("CUSTOMERS");
        Font font = new Font("Verdana", Font.BOLD, 24);
        title.setFont(font);
        headerPanel.add(title);


        JPanel mainPanel = new JPanel();///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        mainPanel.setLayout(new BorderLayout());
        String[] genderOptions = {"None", "Male", "Female", "Other"};

        JPanel coreMainPanel = new JPanel();
        coreMainPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 5;
        c.weighty = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 40, 10, 10);

        JLabel codeLabel = new JLabel("Code");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 0;
        coreMainPanel.add(codeLabel, c);
        JLabel nameLabel = new JLabel("Name");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 1;
        coreMainPanel.add(nameLabel, c);
        JLabel surNameLabel = new JLabel("Surname");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 2;
        coreMainPanel.add(surNameLabel, c);
        JLabel emailLabel = new JLabel("Email");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 3;
        coreMainPanel.add(emailLabel, c);
        JLabel phoneLabel = new JLabel("Phone");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 4;
        coreMainPanel.add(phoneLabel, c);
        JLabel stateLabel = new JLabel("Statement");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 5;
        coreMainPanel.add(stateLabel, c);


        codeText = new JTextField(20);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 0;
        coreMainPanel.add(codeText, c);
        nameText = new JTextField(20);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 1;
        coreMainPanel.add(nameText, c);
        surNameText = new JTextField(20);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 2;
        coreMainPanel.add(surNameText, c);
        emailText = new JTextField(20);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 3;
        coreMainPanel.add(emailText, c);
        phoneText = new JTextField(20);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 4;
        coreMainPanel.add(phoneText, c);
        stateText = new JTextField(20);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 5;
        coreMainPanel.add(stateText, c);


        JSeparator sep = new JSeparator();
        sep.setOrientation(SwingConstants.VERTICAL);


        JPanel rightMainPanel = new JPanel();//////////////////////////////////////////////////////////////////////
        rightMainPanel.setLayout(new BoxLayout(rightMainPanel, BoxLayout.Y_AXIS));
        JButton addCustomerButton = new JButton("ADD CUSTOMER");
        JButton updateCustomerButton = new JButton("UPDATE CUSTOMER");
        JButton deleteCustomerButton = new JButton("DELETE CUSTOMER");
        JButton resetButton = new JButton("Reset");


        rightMainPanel.add(addCustomerButton);
        rightMainPanel.add(updateCustomerButton);
        rightMainPanel.add(deleteCustomerButton);
        rightMainPanel.add(resetButton);


        mainPanel.add(coreMainPanel, BorderLayout.WEST);
        mainPanel.add(sep, BorderLayout.CENTER);
        mainPanel.add(rightMainPanel, BorderLayout.EAST);


        JPanel footerPanel = new JPanel();/////////////////////////////////////////////////////////////////////////////////////////////////////////
        footerPanel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(400, 80));
        table.setFillsViewportHeight(true);
        footerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        tableModel.addColumn("CODE");
        tableModel.addColumn("NAME");
        tableModel.addColumn("SURNAME");
        tableModel.addColumn("EMAIL");
        tableModel.addColumn("PHONE");
        tableModel.addColumn("STATEMENT");
        updateViewFromDatabase();

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(footerPanel, BorderLayout.SOUTH);

        //By clicking in a row from the table the data is displayed in the fields
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {

                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select code, name, surName, email, phone, state from library.customers");
                    DefaultTableModel lineTable = (DefaultTableModel)table.getModel();
                    int SelectedRows = table.getSelectedRow();

                    while (resultSet.next()) {
                        codeText.setText(lineTable.getValueAt(SelectedRows, 0).toString());
                        nameText.setText(lineTable.getValueAt(SelectedRows, 1).toString());
                        surNameText.setText(lineTable.getValueAt(SelectedRows, 2).toString());
                        emailText.setText(lineTable.getValueAt(SelectedRows, 3).toString());
                        phoneText.setText(lineTable.getValueAt(SelectedRows, 4).toString());
                        stateText.setText(lineTable.getValueAt(SelectedRows, 5).toString());
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        addCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    String query = " INSERT INTO customers (code, name, surName, email, phone, state)" + " values (?, ?, ?, ?, ?, ?)";

                    PreparedStatement Pstatement = connection.prepareStatement(query);
                    Pstatement.setString(1,codeText.getText().toString());
                    Pstatement.setString(2,nameText.getText().toString());
                    Pstatement.setString(3,surNameText.getText().toString());
                    Pstatement.setString(4,emailText.getText().toString());
                    Pstatement.setString(5,phoneText.getText().toString());
                    Pstatement.setString(6,stateText.getText().toString());

                    Pstatement.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Customer has been registered Successfully!");
                    connection.close();

                    updateViewFromDatabase();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });

        updateCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultSet = statement.executeQuery("select * from library.customers");

                    resultSet.absolute(table.getSelectedRow() + 1);
                    resultSet.updateString("code", codeText.getText().toString());
                    resultSet.updateString("name", nameText.getText().toString());
                    resultSet.updateString("surName", surNameText.getText().toString());
                    resultSet.updateString("email", emailText.getText().toString());
                    resultSet.updateString("phone", phoneText.getText().toString());
                    resultSet.updateString("state", stateText.getText().toString());
                    resultSet.updateRow();

                    JOptionPane.showMessageDialog(null,"Customer has been updated Successfully!");

                    resultSet.close();
                    statement.close();
                    connection.close();

                    updateViewFromDatabase();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        deleteCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String deleteQuery = "DELETE FROM library.customers WHERE code = " + codeText.getText().toString();
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement = connection.createStatement();
                    int affectedRows = statement.executeUpdate(deleteQuery);

                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(null,"Customer has been deleted Successfully!");
                    } else {
                        System.out.println("Customer has not been deleted yet");
                    }

                    statement.close();
                    connection.close();
                    updateViewFromDatabase();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

       resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                codeText.setText("");
                nameText.setText("");
                surNameText.setText("");
                emailText.setText("");
                phoneText.setText("");
                stateText.setText("");

            }
        });
    }

    public static void updateViewFromDatabase(){

        try {

            tableModel.setRowCount(0);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from library.customers");

            while (resultSet.next()) {

                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                String surName = resultSet.getString("surName");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String state = resultSet.getString("state");

                tableModel.addRow(new Object[]{code, name, surName, email, phone, state});

            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tableModel.fireTableDataChanged();
    }

}


