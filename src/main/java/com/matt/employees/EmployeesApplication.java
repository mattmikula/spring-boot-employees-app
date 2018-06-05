package com.matt.employees;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.matt.employees.strategies.EmployeeCreatorManager;
import com.matt.employees.models.Employee;
import com.matt.employees.services.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class EmployeesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeesApplication.class, args);
    }

    @Bean
    CommandLineRunner init(EmployeeService employeeService) {
        return args -> {
            // Here we use both the Singleton and Strategy patterns to handle creating initial employee data for our application.
            // We currently only support creating initial employees via a JSON file, but using these two patterns allows us to
            // easily add support for other methods in the future.
            //
            // Our manager keeps track of the creation methods it allows (strategies) and will choose the appropriate method
            // depending on the path that is passed in to the manager's createEmployees method. If we wanted to add support for
            // XML files, we would simply use the manager's registerCreator method to supply a strategy to handle creation of
            // employees from an XML file. Because we are using a Singleton to manage our strategies, we only have to register
            // them once instead of each time we wanted to create employees.
            EmployeeCreatorManager.getInstance().registerCreator("json", path -> {

                ObjectMapper mapper = new ObjectMapper();

                // Register the JavaTimeModule to help handle processing dates into LocalDates
                mapper.registerModule(new JavaTimeModule());

                try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
                    return mapper.readValue(inputStream, new TypeReference<List<Employee>>() {
                    });
                } catch (IOException e) {
                    System.out.println("Unable to create employees: " + e.getMessage());
                    return Collections.emptyList();
                }
            });

            List<Employee> employees = EmployeeCreatorManager.getInstance().createEmployees("initial/employees.json");

            employeeService.save(employees);
        };
    }
}
