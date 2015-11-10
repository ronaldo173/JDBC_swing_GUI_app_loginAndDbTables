package GUI.TableModels;

import Core.AuditHistory;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Santer on 10.11.2015.
 */
public class AuditHistoryTableModel extends AbstractTableModel {

    public static final int OBJECT_COL = -1;
    public static final int DATE_TIME = 0;
    private static final int ACTION_COL = 1;
    private static final int USER_FIRST_N_COL = 2;
    private static final int USER_LAST_NAME_COl = 3;

    private String[] colNames = {"Date/Time", "Action", "User First Name",
            "User Last Name"};
    private List<AuditHistory> auditHistories;

    public AuditHistoryTableModel(List<AuditHistory> auditHistories) {
        this.auditHistories = auditHistories;
    }

    @Override
    public int getRowCount() {
        return auditHistories.size();
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        AuditHistory tempAuditHistory = auditHistories.get(rowIndex);
        switch (columnIndex) {
            case DATE_TIME:
                return tempAuditHistory.getActionDateTime();
            case ACTION_COL:
                return tempAuditHistory.getAction();
            case USER_FIRST_N_COL:
                return tempAuditHistory.getUserFirstName();
            case USER_LAST_NAME_COl:
                return tempAuditHistory.getUserLastName();
            default:
                return tempAuditHistory.getUserLastName();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
}
