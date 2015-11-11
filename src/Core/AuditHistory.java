package Core;

import java.util.Date;

/**
 * Created by Santer on 10.11.2015.
 */
public class AuditHistory {
    private int employeeId;
    private String action;
    private Date actionDateTime;

    private String userFirstName, userLastName;

    public AuditHistory( int employeeId, String action, Date actionDateTime, String userFirstName, String userLastName) {
        this.employeeId = employeeId;
        this.action = action;
        this.actionDateTime = actionDateTime;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getActionDateTime() {
        return actionDateTime;
    }

    public void setActionDateTime(Date actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    @Override
    public String toString() {
        return "AuditHistoryDialog{" +

                ", employeeId=" + employeeId +
                ", action='" + action + '\'' +
                ", actionDateTime=" + actionDateTime +
                ", userFirstName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                '}';
    }
}