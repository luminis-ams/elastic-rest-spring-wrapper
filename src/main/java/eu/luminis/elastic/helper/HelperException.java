package eu.luminis.elastic.helper;

/**
 * Exception thrown when something goes wrong in the helper classes.
 */
public class HelperException extends RuntimeException {
    public HelperException(String message) {
        super(message);
    }
}
