package com.matt.employees.controllers;

import com.matt.employees.models.Employee;
import com.matt.employees.models.EmployeeStatus;
import com.matt.employees.services.EmployeeService;
import com.matt.employees.utilities.MapResponseMessage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
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

    @ApiOperation(value = "View a list of all active employees",
            response = Employee.class,
            responseContainer="List")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Employee> getEmployees() {
        return this.employeeService.listActiveEmployees();
    }


    @ApiOperation(value = "View a specific employee", response = Employee.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Resource not found") })
    @RequestMapping(method = RequestMethod.GET, value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEmployee(@PathVariable Long employeeId) {
        Optional<Employee> employee = this.employeeService.getActiveEmployee(employeeId);
        if(!employee.isPresent()){
            return new ResponseEntity<>(MapResponseMessage.createMapResponseFromMessage("Resource not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employee.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "Add an employee")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Resource created successfully"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "Update an employee")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Resource not found")})
    @RequestMapping(method = RequestMethod.PUT, value="/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "Delete an employee",
            notes = "Requires Basic Authentication using a username of 'user' and a password of 'password'",
            authorizations = {@Authorization(value="basicAuth")})
    @ApiResponses(value = { @ApiResponse(code = 202, message = "Resource deleted successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @RequestMapping(method = RequestMethod.DELETE, value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        return this.employeeService.getActiveEmployee(employeeId)
                .map(employee -> {
                    employee.setStatus(EmployeeStatus.INACTIVE);
                    this.employeeService.save(employee);
                    return new ResponseEntity<>(MapResponseMessage.createMapResponseFromMessage("Resource deleted successfully"), HttpStatus.ACCEPTED);
                }).orElse(new ResponseEntity<>(MapResponseMessage.createMapResponseFromMessage("Resource not found"), HttpStatus.NOT_FOUND));
    }

}
