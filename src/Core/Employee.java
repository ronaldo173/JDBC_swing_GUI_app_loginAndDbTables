package Core;

import java.math.BigDecimal;

/**
 * Created by Santer on 09.11.2015.
 */
public class Employee {
    private int id;
    private String lasnName, firstName, email;
    private BigDecimal salary;

    public Employee(int id, String lasnName, String firstName, String email, BigDecimal salary) {
        this.id = id;
        this.lasnName = lasnName;
        this.firstName = firstName;
        this.email = email;
        this.salary = salary;
    }

    public Employee(String lasnName, String firstName, String email, BigDecimal salary) {
        this(0, lasnName, firstName, email, salary);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lasnName;
    }

    public void setLasnName(String lasnName) {
        this.lasnName = lasnName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return String.format("Employee [id=%s, firstName=%s, lastName=%s," +
                "email=%s, salary=%s]", id, firstName, lasnName, email, salary);
    }
}


