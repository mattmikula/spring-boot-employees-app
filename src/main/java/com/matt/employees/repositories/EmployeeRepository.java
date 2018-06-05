package com.matt.employees.repositories;

import com.matt.employees.models.Employee;
import com.matt.employees.models.EmployeeStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Optional<Employee> findByIdAndStatus(Long id, EmployeeStatus status);
    Collection<Employee> findByStatus(EmployeeStatus status);
}

