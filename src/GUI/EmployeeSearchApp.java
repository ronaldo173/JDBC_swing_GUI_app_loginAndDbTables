package GUI;

import Core.Employee;
import DBconnection.DbConnect;
import GUI.TableModels.EmployeeTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.sql.SQLException;

/**
 * Created by Santer on 08.11.2015.
 */
public class EmployeeSearchApp extends JFrame {
    Image icon;
    ImageIcon iconSearchBut, iconExit, iconAdd, iconUpdateBig, iconUpdate, iconHistory;
    DbConnect dbConnect;
    private JPanel panelFromForm;
    private JButton butUpdateEmployee;
    private JButton butAddEmployee;
    private JButton butViewHistory;
    private JTable table1;
    private JButton butSearch;
    private JTextField textFieldLastName;
    private JTextField textFieldLoggedBy;
    private JButton butExit;
    private JPanel panelForTable;

    public EmployeeSearchApp(DbConnect theDbConnect) {
//        super();
        this.dbConnect = theDbConnect;

        icon = Toolkit.getDefaultToolkit().getImage("icons/search.png");
        iconExit = new ImageIcon(new ImageIcon("icons/exit.png").getImage().getScaledInstance(128, 16, Image.SCALE_DEFAULT));
        iconAdd = new ImageIcon(new ImageIcon("icons/add.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
        iconUpdate = new ImageIcon(new ImageIcon("icons/update.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
        iconHistory = new ImageIcon(new ImageIcon("icons/history.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
        iconSearchBut = new ImageIcon(icon.getScaledInstance(24, 24, Image.SCALE_DEFAULT));
        table1.setBackground(Color.GRAY);
        textFieldLoggedBy.setEnabled(false);
        setIconImage(icon);
        setContentPane(panelFromForm);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        butSearch.setIcon(iconSearchBut);
        butExit.setIcon(iconExit);
        butAddEmployee.setIcon(iconAdd);
        butUpdateEmployee.setIcon(iconUpdate);
        butViewHistory.setIcon(iconHistory);
        butAddEmployee.setHorizontalTextPosition(JButton.LEFT);
        butViewHistory.setHorizontalTextPosition(JButton.LEFT);
        butUpdateEmployee.setHorizontalTextPosition(JButton.LEFT);

        pack();
        setLocationRelativeTo(null);

        setVisible(true);
        setTitle("Search App");

        butSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Search");

                try {
                    String lastName = textFieldLastName.getText();
                    System.out.println(lastName);
                    java.util.List<Employee> employeeList = null;

                    if (lastName != null && lastName.trim().length() > 0) {
                        employeeList = dbConnect.searchEmployee(lastName);
                    } else {
                        employeeList = dbConnect.getAllEmployees();
                    }

                    EmployeeTableModel employeeTableModel = new EmployeeTableModel(employeeList);
                    table1.setModel(employeeTableModel);
                    table1.setFont(new Font("Calibri", Font.ITALIC, 16));
//                    panelForTable.add(table1.getTableHeader(), BorderLayout.NORTH);
//                    panelForTable.add(table1, BorderLayout.CENTER);
//                    JScrollPane jScrollPane= new JScrollPane();
//                    jScrollPane.add(panelForTable);

                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(EmployeeSearchApp.this, "Error Search: "
                            + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelFromForm.addComponentListener(new ComponentAdapter() {
        });
        butAddEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEmployees addEmployees = new AddEmployees();
                addEmployees.setVisible(true);
                System.out.println("Add employee");
            }
        });
        butUpdateEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEmployees updateEmployee = new AddEmployees();
                iconUpdateBig = new ImageIcon(new ImageIcon("icons/updateBig.png").getImage().getScaledInstance(168, 168, Image.SCALE_DEFAULT));
                updateEmployee.setLabelAddForPic(iconUpdateBig);
                updateEmployee.setTitle("Update employee");
                updateEmployee.setVisible(true);
                System.out.println("Update employee");
            }
        });
        butViewHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("View history");
            }
        });
        butExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginDialog loginDialog = new LoginDialog();
                    loginDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    loginDialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setTextFieldLoggedBy(String text) {
        this.textFieldLoggedBy.setText(text);
    }
}
