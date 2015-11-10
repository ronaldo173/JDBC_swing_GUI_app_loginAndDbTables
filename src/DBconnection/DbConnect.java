package DBconnection;

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

    public static void main(String[] args) throws IOException, SQLException {
        DbConnect dbConnect = new DbConnect();
        Employee employee = new Employee("Vitaliy", "Klichko", "kli4@mail.ru",
                BigDecimal.valueOf(111000));
        System.out.println(employee);
        dbConnect.addEmployee(employee, 13);
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

    public void updateEmployee(Employee employee, int userId) throws SQLException {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }
        System.out.println("------update------");
    }

    public void addEmployee(Employee employee, int userId) throws SQLException {
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

    private User convertRowToUser(ResultSet resSet) throws SQLException {
        int id = resSet.getInt("id");
        String lastName = resSet.getString("last_name");
        String firstName = resSet.getString("first_name");
        String email = resSet.getString("email");

        return new User(id, lastName, firstName, email);
    }
}
