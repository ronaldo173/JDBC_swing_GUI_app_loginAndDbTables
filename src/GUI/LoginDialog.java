package GUI;

import DBconnection.DbConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;

public class LoginDialog extends JDialog {
    Image icon;
    ImageIcon iconUser, iconPassword;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldUser;
    private JPasswordField passwordField1;
    private JLabel labelUser;
    private JLabel labelPassword;

    public LoginDialog() {
        icon = Toolkit.getDefaultToolkit().getImage("icons/login.png");
        iconUser = new ImageIcon(new ImageIcon("icons/user.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
        iconPassword = new ImageIcon(new ImageIcon("icons/password.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
        //make JLabel with icons
        labelUser.setHorizontalTextPosition(JLabel.RIGHT);
        labelPassword.setHorizontalTextPosition(JLabel.RIGHT);
        labelUser.setIcon(iconUser);
        labelPassword.setIcon(iconPassword);
        passwordField1.setEnabled(false);
        buttonOK.setText("Enter");
        buttonCancel.setText("Exit");

        setIconImage(icon);
        setTitle("Login");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        textFieldUser.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                passwordField1.setEnabled(true);
            }
        });
    }


    private void onOK() throws IOException {
// add your code here
//        dispose();
        try {
            checkPassword();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkPassword() throws IOException, SQLException {
        String nameFromField = textFieldUser.getText();
        String passwordFromField = passwordField1.getText();
//        System.out.println(nameFromField + " - " + passwordFromField);


        if (new DbConnect().checkLoginPassword(nameFromField, passwordFromField)) {
            System.out.println("Entered succesfully");
//            setModal(false);
            setVisible(false);
            dispose();
            EmployeeSearchApp frame = new EmployeeSearchApp(new DbConnect());
            frame.setTextFieldLoggedBy(textFieldUser.getText());
//            frame.setLoggedInUserName(nameFromField);
            frame.setVisible(true);
        } else {
            int tryOkNo = JOptionPane.showConfirmDialog(this, "Login or password not correct! \nTry again?", "Login error",
                    JOptionPane.YES_OPTION);
            if (tryOkNo == 0) {
                textFieldUser.setText(null);
                passwordField1.setText(null);
            } else {
                dispose();
            }
        }
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
