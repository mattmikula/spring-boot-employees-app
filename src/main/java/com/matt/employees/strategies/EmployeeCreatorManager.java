package com.matt.employees.strategies;

import com.matt.employees.models.Employee;
import org.apache.commons.io.FilenameUtils;

import java.util.*;

/**
 * Singleton class that handles keeping track of registered {@link EmployeeCreator} strategies.
 * We use a singleton here so that EmployeeCreator strategies only have to be registered once and can be accessed anywhere in
 * our code. If we didn't use a singleton, we would have to instantiate a manager and register strategies whenever
 * we'd like to use load {@link Employee} objects using this class.
 *
 * Registered EmployeeCreators are stored in an internal map with the file extension as a key.
 * A path to the file is supplied when creating employees via the createEmployees method, and the file's extension is
 * checked against the internal map to find a creator object. If a creator is found, it is called to generate a List of
 * employees. If a creator is not found, an IllegalArgumentException is thrown.
 */
public final class EmployeeCreatorManager {

    private static EmployeeCreatorManager instance = new EmployeeCreatorManager();

    private static Map<String, EmployeeCreator> creators = new HashMap<>();

    // Private constructor to prevent instantiation
    private EmployeeCreatorManager() {}

    /**
     * Static method used to access our private singleton instance.
     *
     * @return - {@link EmployeeCreator} singleton instance
     */
    public static EmployeeCreatorManager getInstance() {
        return instance;
    }

    /**
     * Handles storing a file extension and an {@link EmployeeCreator} in our internal map
     *
     * @param extension - File extension to be associated with the supplied {@link EmployeeCreator}
     * @param creator - An {@link EmployeeCreator} instance that may be used to create {@link Employee} objects
     */
    public void registerCreator(String extension, EmployeeCreator creator) {
        creators.put(extension.toLowerCase(), creator);
    }

    /**
     * Accepts a file path and checks the file's extension against our internal map. If an {@link EmployeeCreator} is
     * associated with the supplied file's extension, we'll use that creator to create a list of {@link Employee} objects.
     * If no creator is associated with the extension, we'll throw an IllegalArgumentException.
     *
     * @param path - Path to a file that will be used to create {@link Employee} objects
     * @return - A list of newly created {@link Employee} objects
     * @throws IllegalArgumentException - Will be thrown if we can't find a registered {@link EmployeeCreator} strategy
     * for the given file path
     */
    public List<Employee> createEmployees(String path) throws IllegalArgumentException {
        String extension = FilenameUtils.getExtension(path).toLowerCase();

        EmployeeCreator creator = creators.get(extension);

        return Optional.ofNullable(creator)
                .map(foundCreator -> foundCreator.createEmployees(path))
                .orElseThrow(IllegalArgumentException::new);
    }
}
