import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Rending extends JPanel{

    Rending(){

        this.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout());
        JButton lendButton = new JButton("LEND");
        JButton returnButton = new JButton("RETURN");
        headerPanel.add(lendButton);headerPanel.add(returnButton);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        JPanel searchEngineCustomer = new JPanel();
        searchEngineCustomer.setLayout(new FlowLayout());
        JLabel customerLabel = new JLabel("Customer's name");
        JTextField customerText = new JTextField(20);
        searchEngineCustomer.add(customerLabel, BorderLayout.NORTH);searchEngineCustomer.add(customerText, BorderLayout.NORTH);
        leftPanel.add(searchEngineCustomer, BorderLayout.NORTH);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        JPanel searchEngineBook = new JPanel();
        searchEngineBook.setLayout(new FlowLayout());
        JLabel bookLabel = new JLabel("Book's name");
        JTextField bookText = new JTextField(20);
        searchEngineBook.add(bookLabel);searchEngineBook.add(bookText);
        rightPanel.add(searchEngineBook, BorderLayout.NORTH);

        JPanel footerPanel = new JPanel();
        JTextField text = new JTextField(60);
        footerPanel.add(text);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(leftPanel, BorderLayout.WEST);this.add(rightPanel, BorderLayout.EAST);
        this.add(footerPanel, BorderLayout.SOUTH);

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement1 = connection1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultSet1 = statement1.executeQuery("select * from library.customers");

                    Connection connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement2 = connection2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultSet2 = statement2.executeQuery("select * from library.books");

                    String codeCustomer = customerText.getText();
                    String codeBook = bookText.getText();

                    while (resultSet1.next()){
                        if (codeCustomer.equals(resultSet1.getString("code"))){
                            if(resultSet1.getString("state").equals("0")){
                                JOptionPane.showMessageDialog(null, "The customer has not been rented any book!");
                                return;
                            }else{
                                while (resultSet2.next()){
                                    if(codeBook.equals(resultSet2.getString("code"))) {
                                        if (resultSet2.getString("state").equals("0")) {
                                            JOptionPane.showMessageDialog(null, "The book is still in the library!");
                                            return;
                                        }else{
                                            resultSet2.updateString("state", "0");
                                            resultSet2.updateRow();
                                            resultSet1.updateString("state", "0");
                                            resultSet1.updateRow();
                                            Customers.updateViewFromDatabase();
                                            Books.updateViewFromDatabase();
                                            JOptionPane.showMessageDialog(null, "The book has been returned successfully to the library from the customer!");
                                            return;
                                        }
                                    }
                                }
                                JOptionPane.showMessageDialog(null, "There is no book with this code!");
                                return;
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "There is no customer with this code!");
                    return;

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                Customers.updateViewFromDatabase();
                Books.updateViewFromDatabase();

            }
        });

        lendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement1 = connection1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultSet1 = statement1.executeQuery("select * from library.customers");

                    Connection connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement2 = connection2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultSet2 = statement2.executeQuery("select * from library.books");

                    String codeCustomer = customerText.getText();
                    String codeBook = bookText.getText();

                    while (resultSet1.next()){

                        if (codeCustomer.equals(resultSet1.getString("code"))){
                            if (resultSet1.getString("state").equals("0")){
                                while (resultSet2.next()){
                                    if (codeBook.equals(resultSet2.getString("code"))){
                                        if(resultSet2.getString("state").equals("0")){
                                            resultSet2.updateString("state", codeCustomer);
                                            resultSet2.updateRow();
                                            resultSet1.updateString("state", codeBook);
                                            resultSet1.updateRow();
                                            Customers.updateViewFromDatabase();
                                            Books.updateViewFromDatabase();
                                            JOptionPane.showMessageDialog(null, "The book has been rented Successfully!");
                                            return;
                                        } else{
                                            JOptionPane.showMessageDialog(null, "The book has been rented arleady!");
                                            return;
                                        }
                                    }
                                }
                                JOptionPane.showMessageDialog(null, "There is no book with this code!");
                                return;
                            }else{
                                JOptionPane.showMessageDialog(null, "The customer has been rented arleady a book!");
                                return;
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "There is no customer with this code!");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


            }
        });

    }
}
