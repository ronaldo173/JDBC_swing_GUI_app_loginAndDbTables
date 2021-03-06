package GUI.TableModels;

import Core.Employee;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Santer on 09.11.2015.
 */
public class EmployeeTableModel extends AbstractTableModel {
    public static final int OBJECT_COL = -1;
    private static final int ID_COL = 0;
    private static final int LAST_NAME_COL = 1;
    private static final int FIRST_NAME_COL = 2;
    private static final int EMAIL_COL = 3;
    private static final int SALARY_COL = 4;

    private String[] colNames = {"id", "Last name", "First name", "Email",
            "Salary"};
    private List<Employee> employees;

    public EmployeeTableModel(List<Employee> employees) {
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
        return getValueAt(1, columnIndex).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);

        switch (columnIndex) {
            case ID_COL:
                return employee.getId();
            case LAST_NAME_COL:
                return employee.getLastName();
            case FIRST_NAME_COL:
                return employee.getFirstName();
            case EMAIL_COL:
                return employee.getEmail();
            case SALARY_COL:
                return employee.getSalary();
            case OBJECT_COL:
                return employee;
            default:
                return employee.getLastName();
        }
    }
}
