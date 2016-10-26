package eu.luminis.elastic.document;

/**
 * Exception thrown when eu.luminis.elastic throws an error.
 */
public class QueryExecutionException extends RuntimeException {
    public QueryExecutionException(String message) {
        super(message);
    }

    public QueryExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
