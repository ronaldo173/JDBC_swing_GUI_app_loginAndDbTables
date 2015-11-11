package GUI;

import Core.Employee;
import DBconnection.DbConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddEmployees extends JDialog {
    ImageIcon iconAdd;
    Image image;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton firstNameButton;
    private JButton lastNameButton;
    private JButton emailButton;
    private JTextField textFieldFirstName;
    private JTextField textFieldLastName;
    private JTextField textFieldEmail;
    private JLabel labelAddForPic;
    private JButton salaryButton;
    private JTextField textFieldSalary;

    private DbConnect dbConnect = null;
    private boolean isUpdateMode = false;
    private Employee previousEmployee = null;
    private EmployeeSearchApp employeeSearchApp = null;

    public AddEmployees() {
        iconAdd = new ImageIcon(new ImageIcon("icons/addBig.png").getImage().getScaledInstance(168, 168, Image.SCALE_DEFAULT));
        image = Toolkit.getDefaultToolkit().getImage("icons/add.png");
        setIconImage(image);
        labelAddForPic.setIcon(iconAdd);

        setTitle("Add Employee");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public AddEmployees(EmployeeSearchApp employeeSearchApp, DbConnect dbConnect) {
        this(employeeSearchApp, dbConnect, null, false);
    }

    public AddEmployees(EmployeeSearchApp employeeSearchApp, DbConnect dbConnect, Employee previousEMployee, boolean isUpdate) {
        this();
        this.dbConnect = dbConnect;
        this.employeeSearchApp = employeeSearchApp;
        this.previousEmployee = previousEMployee;
        isUpdateMode = isUpdate;
        if (isUpdate) {
            insertIntoFormSelected(previousEMployee);
        }
    }

    public static void main(String[] args) {
        AddEmployees dialog = new AddEmployees();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void insertIntoFormSelected(Employee previousEMployee) {
        textFieldFirstName.setText(previousEMployee.getFirstName());
        textFieldLastName.setText(previousEMployee.getLastName());
        textFieldEmail.setText(previousEMployee.getEmail());
        textFieldSalary.setText(previousEMployee.getSalary().toString());
    }

    public void setLabelAddForPic(ImageIcon icon) {
        this.labelAddForPic.setIcon(icon);
    }

    private void onOK() {
        saveEmployee();
        dispose();
    }

    private void saveEmployee() {
        System.out.println("pressed save");
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        BigDecimal salary = convertToBigDecimal(textFieldSalary.getText());

        Employee tempEmployee = null;
        if (isUpdateMode) {
            tempEmployee = previousEmployee;
            tempEmployee.setLasnName(lastName);
            tempEmployee.setFirstName(firstName);
            tempEmployee.setEmail(email);
            tempEmployee.setSalary(salary);
            tempEmployee.setId(previousEmployee.getId());
        } else {
            tempEmployee = new Employee(lastName, firstName, email, salary);
        }

        //save to db
        try {
            if (isUpdateMode) {
                dbConnect.updateEmployee(tempEmployee, tempEmployee.getId());
            } else {
                dbConnect.addEmployee(tempEmployee, getLastIdIncr());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setVisible(false);
        dispose();
        try {
            employeeSearchApp.refrestEmployeeView();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(employeeSearchApp, "Employee saves succesfully!",
                "Saved!", JOptionPane.INFORMATION_MESSAGE);

    }

    private int getLastIdIncr() {
        int id = 0;
        Connection connection = dbConnect.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT max(id) from employees");
            while (resultSet.next()) {
                id = resultSet.getInt(1) +1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("GetlastId +1: " + id);

        return id;
    }

    private BigDecimal convertToBigDecimal(String text) {
        BigDecimal bigDecimal = null;

        try {
            double temp = Double.parseDouble(text);
            bigDecimal = BigDecimal.valueOf(temp);
        } catch (Exception e) {
            System.out.println("Error in " + this.getName());
            bigDecimal = BigDecimal.valueOf(0.0);
        }
        return bigDecimal;
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
