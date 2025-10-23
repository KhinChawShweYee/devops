package com.napier.devops;

/**
 * Represents an employee
 */
public class Employee
{
    /**
     * Employee number
     */
    public int emp_no;

    /**
     * Employee's first name
     */
    public String first_name;

    /**
     * Employee's last name
     */
    public String last_name;

    /**
     * Employee's job title
     */
    public String title;

    /**
     * Employee's salary
     */
    public int salary;

    /**
     * Employee's current department
     */
    public Department dept_name;

    /**
     * Employee's manager
     */
    public Employee manager;

    @Override
    public String toString() {
        return "Employee{" +
                "emp_no=" + emp_no +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", title='" + title + '\'' +
                ", salary=" + salary +
                ", dept_name=" + dept_name +
                ", manager=" + manager +
                '}';
    }
}

