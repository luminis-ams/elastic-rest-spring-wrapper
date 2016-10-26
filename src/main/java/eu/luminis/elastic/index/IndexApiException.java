package eu.luminis.elastic.index;


import eu.luminis.elastic.ElasticClientException;

/**
 * Exception thrown when having problems interacting with the index api.
 */
public class IndexApiException extends ElasticClientException {
    public IndexApiException(String message) {
        super(message);
    }

    public IndexApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
