package com.matt.employees.strategies;

import com.matt.employees.models.Employee;
import org.apache.commons.io.FilenameUtils;

import java.util.*;

/**
 * Singleton class that handles keeping track of registered {@link EmployeeCreator} strategies.
 * We use a singleton here so that EmployeeCreator strategies only have to be registered once and can be accessed anywhere in
 * our code instead of having to instantiate a manager and register strategies whenever we'd like to use load employees.
 *
 * Registered EmployeeCreators are stored in an internal hash with the file extension as a key.
 * A path to the file is supplied when creating employees via the createEmployees method, and the file's extension is
 * checked against the internal hash to find a creator object. If a creator is found, it is called to generate a List of
 * employees. If a creator is not found, an IllegalArgumentException is thrown.
 */
public final class EmployeeCreatorManager {

    private static EmployeeCreatorManager instance = new EmployeeCreatorManager();

    private static Map<String, EmployeeCreator> creators = new HashMap<>();

    // Private constructor to prevent instantiation
    private EmployeeCreatorManager() {}

    public static EmployeeCreatorManager getInstance() {
        return instance;
    }

    public void registerCreator(String extension, EmployeeCreator creator) {
        creators.put(extension.toLowerCase(), creator);
    }

    public List<Employee> createEmployees(String path) throws IllegalArgumentException {
        String extension = FilenameUtils.getExtension(path).toLowerCase();

        EmployeeCreator creator = creators.get(extension);

        return Optional.ofNullable(creator)
                .map(foundCreator -> foundCreator.createEmployees(path))
                .orElseThrow(IllegalArgumentException::new);
    }
}
