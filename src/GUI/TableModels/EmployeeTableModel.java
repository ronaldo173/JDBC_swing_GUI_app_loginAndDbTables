package GUI.TableModels;

import Core.Employee;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Santer on 09.11.2015.
 */
public class EmployeeTableModel extends AbstractTableModel {
    public static final int OBJECT_COL = -1;
    private static final int LAST_NAME_COL = 0;
    private static final int FIRST_NAME_COL = 1;
    private static final int EMAIL_COL = 2;
    private static final int SALARY_COL = 3;

    private String[] colNames = {"Last name", "First name", "Email",
            "Salary"};
    private List<Employee> employees;

    public EmployeeTableModel(List<Employee> employees){
        this.employees = employees;
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);

        switch (columnIndex){
            case LAST_NAME_COL:
                return employee.getLasnName();
            case FIRST_NAME_COL:
                return employee.getFirstName();
            case EMAIL_COL:
                return employee.getEmail();
            case OBJECT_COL:
                return employee;
            default:
                return employee.getLasnName();
        }
    }
}
