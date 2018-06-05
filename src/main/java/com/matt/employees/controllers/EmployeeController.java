package com.matt.employees.controllers;

import com.matt.employees.models.Employee;
import com.matt.employees.models.EmployeeStatus;
import com.matt.employees.services.EmployeeService;
import com.matt.employees.utilities.MapResponseMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ApiOperation(value="View a list of all active employees")
    @RequestMapping(method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Employee> getEmployees() {
        return this.employeeService.listActiveEmployees();
    }

    @ApiOperation(value="View a specific employee")
    @RequestMapping(method = RequestMethod.GET, value = "/{employeeId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEmployee(@PathVariable Long employeeId) {
        Optional<Employee> employee = this.employeeService.getActiveEmployee(employeeId);
        if(!employee.isPresent()){
            return new ResponseEntity<>(MapResponseMessage.createMapResponseFromMessage("Resource not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employee.get(), HttpStatus.OK);
    }

    @ApiOperation(value="Add an employee")
    @RequestMapping(method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addEmployee(@RequestBody Employee input) {
        this.employeeService.save(
                new Employee(input.getFirstName(),
                        input.getMiddleInitial(),
                        input.getLastName(),
                        input.getDateOfBirth(),
                        input.getDateOfEmployment(),
                        EmployeeStatus.ACTIVE)
        );
        return new ResponseEntity<>(MapResponseMessage.createMapResponseFromMessage("Resource created successfully"), HttpStatus.CREATED);
    }

    @ApiOperation(value="Update an employee")
    @RequestMapping(method = RequestMethod.PUT, value="/{employeeId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee input) {
        return this.employeeService.getActiveEmployee(employeeId)
                .map(employee -> {
                    employee.setFirstName(input.getFirstName());
                    employee.setMiddleInitial(input.getMiddleInitial());
                    employee.setLastName(input.getLastName());
                    employee.setDateOfBirth(input.getDateOfBirth());
                    employee.setDateOfEmployment(input.getDateOfEmployment());
                    this.employeeService.save(employee);
                    return new ResponseEntity<>(MapResponseMessage.createMapResponseFromMessage("Resource updated successfully"), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(MapResponseMessage.createMapResponseFromMessage("Error updating resource"), HttpStatus.BAD_REQUEST));
    }

    @ApiOperation(value="Delete an employee")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{employeeId}", produces=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        return this.employeeService.getActiveEmployee(employeeId)
                .map(employee -> {
                    employee.setStatus(EmployeeStatus.INACTIVE);
                    this.employeeService.save(employee);
                    return new ResponseEntity<>(MapResponseMessage.createMapResponseFromMessage("Resource deleted successfully"), HttpStatus.ACCEPTED);
                }).orElse(new ResponseEntity<>(MapResponseMessage.createMapResponseFromMessage("Resource not found"), HttpStatus.NOT_FOUND));
    }

}
