package com.matt.employees.strategies;

import com.matt.employees.models.Employee;

import java.util.List;

/**
 * Interface for defining an EmployeeCreator used by {@link EmployeeCreatorManager}
 *
 * EmployeeCreators are strategies that are used to create employee objects from files. Using a strategy pattern here
 * allows us to register a list of creators with our EmployeeCreatorManager and our code will choose which creation method
 * to use at runtime.
 */
public interface EmployeeCreator {
    List<Employee> createEmployees(String path);
}
