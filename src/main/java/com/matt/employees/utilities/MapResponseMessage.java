package com.matt.employees.utilities;

import java.util.Collections;
import java.util.Map;

/**
 * Utility class to return a map object with a key of "response" and the supplied method.
 *
 * Used to return more than a simple string in a response from the controller.
 */
public class MapResponseMessage {
    public static Map<String, String> createMapResponseFromMessage(String message) {
        return Collections.singletonMap("response", message);
    }
}
