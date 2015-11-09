package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    public static void main(String[] args) {
        AddEmployees dialog = new AddEmployees();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void setLabelAddForPic(ImageIcon icon) {
        this.labelAddForPic.setIcon(icon);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
