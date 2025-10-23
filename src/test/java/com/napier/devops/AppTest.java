package com.napier.devops;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
    void printSalariesTestNull()
    {
        app.printSalaries(null);
    }

    @Test
    void printSalariesTestEmpty()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        app.printSalaries(employees);
    }

    @Test
    void printSalariesTestContainsNull()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(null);
        app.printSalaries(employees);
    }

    @Test
    void printSalaries()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.printSalaries(employees);
    }

    @Test
    void displayEmployeeNull() {
        app.displayEmployee(null);
    }


    @Test
    void displayEmployeeNoManagerOrDept() {
        Employee emp = new Employee();
        emp.emp_no = 2;
        emp.first_name = "Alice";
        emp.last_name = "Smith";
        emp.title = "Manager";
        emp.salary = 65000;
        emp.dept_name = null;
        emp.manager = null;

        app.displayEmployee(emp);
    }

    @Test
    void displayEmployeeSimple() {
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;

        Department dept = new Department();
        dept.dept_name = "Engineering";
        emp.dept_name = dept;

        Employee manager = new Employee();
        manager.first_name = "Jane Doe";
        emp.manager = manager;

        app.displayEmployee(emp);
    }

    @Test
    void displayEmployeeTestMissingFields()
    {
        Employee emp = new Employee();
        emp.emp_no = 2;
        emp.first_name = null;
        emp.last_name = "Smith";
        emp.title = null;
        emp.salary = 0;
        emp.dept_name = null;
        emp.manager = null;
        app.displayEmployee(emp);
    }

    @Test
    void displayEmployeeTestNegativeSalary()
    {
        // Arrange: create department and manager
        Department dept = new Department();
        dept.dept_name = "Finance";

        Employee mgr = new Employee();
        mgr.first_name = "Jane";
        mgr.last_name = "Smith";

        Employee emp = new Employee();
        emp.emp_no = 3;
        emp.first_name = "Test";
        emp.last_name = "User";
        emp.title = "Intern";
        emp.salary = -1000;
        emp.dept_name = dept;
        emp.manager = mgr;

        // Redirect System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act
        app.displayEmployee(emp);

        // Reset System.out
        System.setOut(System.out);

        // Assert: check the expected printed text
        String expectedOutput = """
                3 Test User
                Intern
                Salary: [Invalid]
                Finance
                Manager: Jane Smith
                """;

        assertTrue(outContent.toString().contains("Salary: [Invalid]"));
        assertTrue(outContent.toString().contains("Finance"));
        assertTrue(outContent.toString().contains("Manager: Jane Smith"));
    }

}