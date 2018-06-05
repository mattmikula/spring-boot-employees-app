package com.matt.employees.services;

import com.matt.employees.models.Employee;
import com.matt.employees.models.EmployeeStatus;
import com.matt.employees.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class to handle our application level data access. For the most part we only want to interact with
 * active employees, so we create an easy interface for calling code to interact with the {@link EmployeeRepository}.
 */
@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Iterable<Employee> listActiveEmployees() {
        return this.employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
    }

    public Optional<Employee> getActiveEmployee(Long employeeId) {
        return this.employeeRepository.findByIdAndStatus(employeeId, EmployeeStatus.ACTIVE);
    }

    public Employee save(Employee employee) {
        return this.employeeRepository.save(employee);
    }

    public Iterable<Employee> save(List<Employee> employees) {
        return this.employeeRepository.saveAll(employees);
    }
}
