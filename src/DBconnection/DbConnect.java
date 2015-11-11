package DBconnection;

import Core.AuditHistory;
import Core.Employee;
import Core.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Santer on 09.11.2015.
 */
public class DbConnect {
    private Connection connection;

    public DbConnect() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("propDemo.properties"));

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");

//        System.out.println(user + " - " + password + " - " + url);
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connected to db: " + url);
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean checkLoginPassword(String log, String pass) throws SQLException {
        Statement statement = null;

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select  last_name, password from users");

            while (resultSet.next()) {
                String dbLogin = resultSet.getString("last_name");
                String dbPassword = resultSet.getString("password");
                if (log.equals(dbLogin) && pass.equals(dbPassword)) {
                    System.out.println("Login:  is right,password too, you entered as:" + log);
                    return true;
                }
            }
            return false;
        } finally {
            close(statement);
        }
    }

    public void updateEmployee(Employee employee, int employeeId) throws SQLException {
        System.out.println("method updateEmployee");
        System.out.println(employee);
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE employees" +
                    " set first_name = ?, last_name = ?, email = ?, salary = ? " +
                    "WHERE id = ?");

            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setBigDecimal(4, employee.getSalary());
            preparedStatement.setInt(5, employee.getId());

            preparedStatement.executeUpdate();

            ///*
            //audit
            preparedStatement = connection.prepareStatement("INSERT INTO audit_history" +
                    "(employee_id, action, action_date_time) VALUES " +
                    "(?, ?, ?)");

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, "Update employee");
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }
        System.out.println("------update------");
    }

    public void addEmployee(Employee employee, int employeeId) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT  INTO employees (first_name, last_name, email, salary)" +
                    " VALUES (?, ?, ?, ?)");

            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setBigDecimal(4, employee.getSalary());
//            preparedStatement.setInt();

            preparedStatement.executeUpdate();

            ///
            ///Add audit history table db
            //
            employee.setId(employeeId);
            System.out.println("**from add employee " + employee.getId());
            preparedStatement = connection.prepareStatement("Insert into audit_history ( employee_id," +
                    "action, action_date_time) VALUES ( ?, ?, ?)");
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.setString(2, "Added new employee");
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            preparedStatement.executeUpdate();
        } finally {
            close(preparedStatement);
        }

        System.out.println("------add------");
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employeeList = new ArrayList<>();

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * from employees order by id");

            while (resultSet.next()) {
                Employee employee = convertRowToEmployee(resultSet);
                employeeList.add(employee);
            }
            return employeeList;
        } finally {
            close(statement, resultSet);
        }
    }

    public List<Employee> searchEmployee(String lastName) throws SQLException {
        List<Employee> employeeList = new ArrayList<>();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            lastName += "%";
            statement = connection.prepareStatement("SELECT * FROM employees WHERE " +
                    "last_name like ? ");
            statement.setString(1, lastName);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = convertRowToEmployee(resultSet);
                employeeList.add(employee);
            }
            return employeeList;
        } finally {
            close(statement, resultSet);
        }
    }

    private Employee convertRowToEmployee(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String lastName = resultSet.getString("last_name");
        String firstName = resultSet.getString("first_Name");
        String email = resultSet.getString("email");
        BigDecimal salary = resultSet.getBigDecimal("salary");

        Employee employee = new Employee(id, lastName, firstName, email, salary);
        return employee;
    }

    private void close(Statement statement, ResultSet resultSet) throws SQLException {
        close(null, statement, resultSet);
    }

    private void close(Statement preparedStatement) throws SQLException {
        close(null, preparedStatement, null);
    }

    private void close(Connection connect, Statement preparedStatement, ResultSet resSet) throws SQLException {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (connect != null) {
            connect.close();
        }
    }

    public List<User> getUsers() throws SQLException {
        List<User> list = new ArrayList<>();

        Statement statement = null;
        ResultSet resSet = null;
        try {
            statement = connection.createStatement();
            resSet = statement.executeQuery("SELECT  * from users");

            while (resSet.next()) {
                User user = convertRowToUser(resSet);
                list.add(user);
            }
            return list;
        } finally {
            close(statement, resSet);
        }
    }

    //history of one
    public List<AuditHistory> getAuditHIstory(int employeeID) throws SQLException {
        List<AuditHistory> list = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();

            String mysql = "select employee_id, action, action_date_time, first_name, last_name " +
                    "from audit_history INNER JOIN employees " +
                    "where employee_id=" + employeeID + " AND employees.id = " + employeeID;

            resultSet = statement.executeQuery(mysql);

            while (resultSet.next()) {
                String action = resultSet.getString("action");
                Timestamp timestamp = resultSet.getTimestamp("action_date_time");
                Date actionDateTime = new Date(timestamp.getTime());
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                AuditHistory temp = new AuditHistory(employeeID, action, actionDateTime, userFirstName, userLastName);
                list.add(temp);
            }
//            for (AuditHistory auditHistory : list) {
//                System.out.println(auditHistory);
//            }
            return list;

        } finally {
            close(statement, resultSet);
        }
    }

    //all history
    public List<AuditHistory> getAuditHIstoryAll() throws SQLException {
        List<AuditHistory> list = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();

            String mysql = "select employee_id, action, action_date_time, first_name, last_name " +
                    "from audit_history INNER JOIN employees  ON employee_id = employees.id";
            resultSet = statement.executeQuery(mysql);

            while (resultSet.next()) {
                int employeeID = resultSet.getInt("employee_id");
                String action = resultSet.getString("action");
                Timestamp timestamp = resultSet.getTimestamp("action_date_time");
                Date actionDateTime = new Date(timestamp.getTime());
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                AuditHistory temp = new AuditHistory(employeeID, action, actionDateTime, userFirstName, userLastName);
                list.add(temp);
            }
//            for (AuditHistory auditHistory : list) {
//                System.out.println(auditHistory);
//            }
            return list;

        } finally {
            close(statement, resultSet);
        }
    }

    private User convertRowToUser(ResultSet resSet) throws SQLException {
        int id = resSet.getInt("id");
        String lastName = resSet.getString("last_name");
        String firstName = resSet.getString("first_name");
        String email = resSet.getString("email");

        return new User(id, lastName, firstName, email);
    }

    public void clearAuditHstoryDB() {
        //at start of app we clear audit history table
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate("DELETE from audit_history WHERE id>0");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void deleteEmployee(int selectedIdEmployee) {
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM employees WHERE id = " + selectedIdEmployee);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
