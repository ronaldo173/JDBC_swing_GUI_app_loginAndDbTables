package GUI;

import Core.AuditHistory;
import Core.Employee;
import GUI.TableModels.AuditHistoryTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditHistoryDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel labelEmployee;
    private JTable table1;
    private JButton buttonCancel;
    private Image image;

    public AuditHistoryDialog() {
        image = Toolkit.getDefaultToolkit().getImage("icons/history.png");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("View History");
        setIconImage(image);
        pack();
        setLocationRelativeTo(null);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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
        AuditHistoryDialog dialog = new AuditHistoryDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
// add your code here
        setVisible(false);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public void populate(Employee tempEmployee, java.util.List<AuditHistory> auditHistoryList) {
        if (tempEmployee != null) {
            labelEmployee.setText(tempEmployee.getFirstName() + " " + tempEmployee.getLastName());
        } else {
            labelEmployee.setText("All employees history");
        }

        AuditHistoryTableModel model = new AuditHistoryTableModel(auditHistoryList);
        table1.setModel(model);

        TableCellRenderer tableCellRenderer = new DateTimeCellRender();
        table1.getColumnModel().getColumn(AuditHistoryTableModel.DATE_TIME).setCellRenderer(tableCellRenderer);
    }


    private final class DateTimeCellRender extends DefaultTableCellRenderer {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd//MM//yy hh:mm:ss");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Date) {
                value = dateFormat.format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
