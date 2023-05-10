import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Books extends JPanel{


    static DefaultTableModel tableModel;
    JTable table;

    JTextField codeText;
    JTextField titleText;
    JTextField stateText;


    Books(){


        this.setLayout(new BorderLayout());

        //Panel for Title
        JPanel headerPanel = new JPanel();/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        headerPanel.setLayout(new FlowLayout());
        JLabel headerLabel = new JLabel("BOOKS");
        Font font = new Font("Verdana", Font.BOLD, 24);
        headerLabel.setFont(font);
        headerPanel.add(headerLabel);


        JPanel mainPanel = new JPanel();///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        mainPanel.setLayout(new BorderLayout());

        JPanel leftMainPanel = new JPanel();
        leftMainPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 2;
        c.weighty = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 40, 10, 10);

        JLabel codeLabel = new JLabel("Code");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 0;
        leftMainPanel.add(codeLabel, c);
        JLabel titleLabel = new JLabel("Title");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 1;
        leftMainPanel.add(titleLabel, c);
        JLabel stateLabel = new JLabel("Availability");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 2;
        leftMainPanel.add(stateLabel, c);
        codeText = new JTextField(20);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 0;
        leftMainPanel.add(codeText, c);
        titleText = new JTextField(20);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 1;
        leftMainPanel.add(titleText, c);
        stateText = new JTextField(20);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 2;
        leftMainPanel.add(stateText, c);

        JSeparator sep = new JSeparator();
        sep.setOrientation(SwingConstants.VERTICAL);

        JPanel rightMainPanel = new JPanel();//////////////////////////////////////////////////////////////////////
        rightMainPanel.setLayout(new BoxLayout(rightMainPanel, BoxLayout.Y_AXIS));
        JButton addButton = new JButton("ADD A NEW BOOK");
        JButton deleteButton = new JButton("DELETE A BOOK");

        rightMainPanel.add(addButton);
        rightMainPanel.add(deleteButton);

        mainPanel.add(leftMainPanel, BorderLayout.WEST);
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
        tableModel.addColumn("TITLE");
        tableModel.addColumn("VALUE");
        updateViewFromDatabase();
        this.add(headerPanel, BorderLayout.NORTH);this.add(mainPanel, BorderLayout.CENTER);this.add(footerPanel, BorderLayout.SOUTH);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {

                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from library.books");
                    DefaultTableModel lineTable = (DefaultTableModel)table.getModel();
                    int SelectedRows = table.getSelectedRow();

                    while (resultSet.next()) {
                        codeText.setText(lineTable.getValueAt(SelectedRows, 0).toString());
                        titleText.setText(lineTable.getValueAt(SelectedRows, 1).toString());
                        stateText.setText(lineTable.getValueAt(SelectedRows, 2).toString());
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    String query = " INSERT INTO books (code, title, state)" + " values (?, ?, ?)";

                    PreparedStatement Pstatement = connection.prepareStatement(query);
                    Pstatement.setString(1,codeText.getText().toString());
                    Pstatement.setString(2,titleText.getText().toString());
                    Pstatement.setString(3,stateText.getText().toString());

                    Pstatement.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Customer has been registered Successfully!");
                    connection.close();

                    updateViewFromDatabase();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String deleteQuery = "DELETE FROM library.books WHERE code = " + codeText.getText().toString();
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
                    Statement statement = connection.createStatement();
                    int affectedRows = statement.executeUpdate(deleteQuery);

                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(null,"Customer has been deleted Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null,"Customer has not been deleted yet");
                    }

                    statement.close();
                    connection.close();
                    updateViewFromDatabase();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }


    public static void updateViewFromDatabase(){

        try {

            tableModel.setRowCount(0);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "6979863701sp");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from library.books");

            while (resultSet.next()) {

                String code = resultSet.getString("code");
                String title = resultSet.getString("title");
                String state = resultSet.getString("state");

                tableModel.addRow(new Object[]{code, title, state});

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
