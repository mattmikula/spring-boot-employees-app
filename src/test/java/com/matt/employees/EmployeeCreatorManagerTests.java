package com.matt.employees;

import com.matt.employees.strategies.EmployeeCreatorManager;
import com.matt.employees.models.Employee;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class EmployeeCreatorManagerTests {

    @Before
    public void setup() {
        EmployeeCreatorManager.getInstance().registerCreator("json", path -> {
            List<Employee> employeeList = new ArrayList<>();
            employeeList.add(Mockito.mock(Employee.class));
            return employeeList;
        });
    }

    /**
     * Ensure that the factory's createEmployees returns a list of 1 employee for a json file since we registered a
     * creator that handles json files.
     */
    @Test
    public void testValidFileType() {
        List<Employee> employees = EmployeeCreatorManager.getInstance().createEmployees("foo.json");
        assertEquals(employees.size(), 1);
        assertThat(employees.get(0), instanceOf(Employee.class));
    }

    /**
     * Ensure that the factory's createEmployees throws an IllegalArgumentException for an XML file since we only
     * registered a creator for JSON files.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFileType() {
        EmployeeCreatorManager.getInstance().createEmployees("foo.xml");
    }
}



