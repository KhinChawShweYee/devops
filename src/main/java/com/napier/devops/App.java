package com.napier.devops;

import java.sql.*;
import java.util.ArrayList;

public class App
{

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }


    public Employee getEmployee(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, " +
                            "       t.title, s.salary, d.dept_name, " +
                            "       CONCAT(m.first_name, ' ', m.last_name) AS manager " +
                            "FROM employees e " +
                            "     JOIN titles t ON e.emp_no = t.emp_no AND t.to_date = '9999-01-01' " +
                            "     JOIN salaries s ON e.emp_no = s.emp_no AND s.to_date = '9999-01-01' " +
                            "     JOIN dept_emp de ON e.emp_no = de.emp_no AND de.to_date = '9999-01-01' " +
                            "     JOIN departments d ON de.dept_no = d.dept_no " +
                            "     JOIN dept_manager dm ON d.dept_no = dm.dept_no AND dm.to_date = '9999-01-01' " +
                            "     JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE e.emp_no = ?";
            // Use PreparedStatement
            PreparedStatement pstmt = con.prepareStatement(strSelect);
            pstmt.setInt(1, ID);

            // Execute query
            ResultSet rset = pstmt.executeQuery();
            if (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");

                // --- FIX 1: Department Object Assignment ---
                // Create a new Department object
                Department dept = new Department();
                // Set the department name using the String from the result set
                dept.dept_name = rset.getString("dept_name");
                // Assign the new Department object to the Employee field
                emp.dept_name = dept;

                // --- FIX 2: Manager (Employee) Object Assignment ---
                // Create a new Employee object for the manager
                Employee manager = new Employee();
                // Since the query returns the manager's name as a single concatenated String,
                // we'll store it in the manager's 'first_name' field for display purposes.
                manager.first_name = rset.getString("manager");
                // Assign the new Manager (Employee) object to the main Employee's manager field
                emp.manager = manager;

                return emp;
            }
            else
            {
                System.out.println("No employee found with ID: " + ID);
                return null;
            }

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public ArrayList<Employee> getSalariesByRole(String title)
    {
        ArrayList<Employee> employees = new ArrayList<>();
        try
        {
            // Create SQL statement
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, s.salary " +
                            "FROM employees e, salaries s, titles t " +
                            "WHERE e.emp_no = s.emp_no " +
                            "AND e.emp_no = t.emp_no " +
                            "AND s.to_date = '9999-01-01' " +
                            "AND t.to_date = '9999-01-01' " +
                            "AND t.title = ? " +
                            "ORDER BY e.emp_no ASC";

            PreparedStatement pstmt = con.prepareStatement(strSelect);
            pstmt.setString(1, title);

            // Execute query
            ResultSet rset = pstmt.executeQuery();

            // Process results
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = rset.getInt("salary");
                employees.add(emp);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salaries by role");
        }
        return employees;
    }


    public void displayEmployee(Employee emp)
    {
        if (emp != null)
        {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept_name + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
        else
            System.out.println("Employee is null");
    }



    //Use case 1
    /**
     * Gets all the current employees and salaries.
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Prints a list of employees.
     * @param employees The list of employees to print.
     */
    public void printSalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }



    /**
     * Gets a department's details by its name.
     * Assumes a Department class exists with fields like dept_no and dept_name.
     *
     * @param dept_name The name of the department to retrieve.
     * @return The Department object, or null if not found.
     */
    public Department getDepartment(String dept_name)
    {
        if (con == null || dept_name == null || dept_name.trim().isEmpty()) {
            System.out.println("Invalid department name or database connection is null.");
            return null;
        }

        try
        {
            // Create string for SQL statement
            String strSelect = "SELECT dept_no, dept_name FROM departments WHERE dept_name = ?";

            // Use PreparedStatement
            PreparedStatement pstmt = con.prepareStatement(strSelect);
            pstmt.setString(1, dept_name);

            // Execute query
            ResultSet rset = pstmt.executeQuery();

            // Extract department information
            if (rset.next())
            {
                // NOTE: This assumes a 'Department' class is defined elsewhere
                // with fields: dept_no and dept_name.
                Department dept = new Department();
                dept.dept_no = rset.getString("dept_no");
                dept.dept_name = rset.getString("dept_name");
                return dept;
            }
            else
            {
                System.out.println("No department found with name: " + dept_name);
                return null;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details for: " + dept_name);
            return null;
        }
    }

    /**
     * Gets all the current employees and salaries for a specific department.
     * @param dept The Department object containing the department's name.
     * @return A list of all employees and salaries in that department, or an empty list if there is an error.
     */
    public ArrayList<Employee> getSalariesByDepartment(Department dept)
    {
        ArrayList<Employee> employees = new ArrayList<>();
        // Check if the Department object is valid (e.g., has a name to search by)
        if (dept == null || dept.dept_name == null || con == null) {
            System.out.println("Invalid department object or database connection is null.");
            return employees; // Return empty list
        }

        try
        {
            // Create string for SQL statement using JOINs for clarity and efficiency
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, s.salary " +
                            "FROM employees e " +
                            "JOIN salaries s ON e.emp_no = s.emp_no AND s.to_date = '9999-01-01' " +
                            "JOIN dept_emp de ON e.emp_no = de.emp_no AND de.to_date = '9999-01-01' " +
                            "JOIN departments d ON de.dept_no = d.dept_no " +
                            "WHERE d.dept_name = ? " +
                            "ORDER BY e.emp_no ASC";

            // Use PreparedStatement to prevent SQL injection and set the parameter
            PreparedStatement pstmt = con.prepareStatement(strSelect);
            pstmt.setString(1, dept.dept_name); // Set the department name parameter

            // Execute SQL statement
            ResultSet rset = pstmt.executeQuery();

            // Extract employee information
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = rset.getInt("salary");
                employees.add(emp);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details for department: " + dept.dept_name);
            employees.clear(); // Clear any partial results in case of error
        }
        return employees;
    }

    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();
        // Get Employee
//        Employee emp = a.getEmployee(490758);
        // Display results
//        a.displayEmployee(emp);



        // Example: Get all salaries for "Engineer"
//        ArrayList<Employee> engineers = a.getSalariesByRole("Engineer");
//        a.displayEmployees(engineers);



        // Extract employee salary information
//        ArrayList<Employee> employees = a.getAllSalaries();

        // Test the size of the returned data - should be 240124
//        System.out.println(employees.size());


        //get Department by name
        Department department = a.getDepartment("Sales");

        //Get salary by Department
        ArrayList<Employee> employees = a.getSalariesByDepartment(department);

        //print all salaries
        a.printSalaries(employees);
        // Disconnect from database
        a.disconnect();
    }


}