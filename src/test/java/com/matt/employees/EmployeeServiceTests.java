package com.matt.employees;

import com.matt.employees.models.Employee;
import com.matt.employees.models.EmployeeStatus;
import com.matt.employees.repositories.EmployeeRepository;
import com.matt.employees.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests to ensure the correct methods on our {@link EmployeeRepository} are called with the correct arguments when using
 * our service layer. We mock the employee repository itself because these tests shouldn't be concerned with the
 * implementation details of that layer.
 */
public class EmployeeServiceTests {
    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;

    private Employee activeEmployee;
    private List<Employee> employees;

    @Before
    public void setup() {
        this.employeeRepository = mock(EmployeeRepository.class);
        this.employeeService = new EmployeeService(employeeRepository);
        this.activeEmployee = mock(Employee.class);
        this.employees = new ArrayList<>();
        this.employees.add(this.activeEmployee);
    }

    @Test
    public void testListActiveEmployees() {
        when(this.employeeRepository.findByStatus(EmployeeStatus.ACTIVE)).thenReturn(this.employees);

        List<Employee> returnedEmployees = (List<Employee>) this.employeeService.listActiveEmployees();

        // Confirm that the expected repository level call was made by the service
        verify(this.employeeRepository).findByStatus(EmployeeStatus.ACTIVE);
        // Confirm that the repository response was passed through the service
        assertEquals(returnedEmployees, this.employees);
    }

    @Test
    public void testGetActiveEmployee() {
        when(this.employeeRepository.findByIdAndStatus(this.activeEmployee.getId(), EmployeeStatus.ACTIVE))
                .thenReturn(Optional.of(this.activeEmployee));

        Optional<Employee> returnedEmployee = this.employeeService.getActiveEmployee(this.activeEmployee.getId());

        // Confirm that the expected repository level call was made by the service
        verify(this.employeeRepository).findByIdAndStatus(this.activeEmployee.getId(), EmployeeStatus.ACTIVE);
        // Confirm that the repository response was passed through the service
        assertEquals(returnedEmployee.get(), this.activeEmployee);
    }

    @Test
    public void testSaveSingleEmployee() {
        when(this.employeeRepository.save(this.activeEmployee)).thenReturn(this.activeEmployee);

        Employee savedEmployee = this.employeeService.save(this.activeEmployee);

        // Confirm that the expected repository level call was made by the service
        verify(this.employeeRepository).save(this.activeEmployee);
        // Confirm that the repository response was passed through the service
        assertEquals(savedEmployee, this.activeEmployee);
    }

    @Test
    public void testSaveMultipleEmployees() {
        when(this.employeeRepository.saveAll(this.employees)).thenReturn(this.employees);

        List<Employee> savedEmployees = (List<Employee>) this.employeeService.save(this.employees);

        // Confirm that the expected repository level call was made by the service
        verify(this.employeeRepository).saveAll(this.employees);
        // Confirm that the repository response was passed through the service
        assertEquals(savedEmployees, this.employees);
    }
}
