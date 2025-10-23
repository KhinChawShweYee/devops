package com.napier.devops;

public class Department {

    public String dept_no;
    public String dept_name;
    public Employee manager;

    @Override
    public String toString() {
        return dept_name;  // prints just the name instead of memory reference
    }
}
