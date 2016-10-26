package eu.luminis.elastic;

/**
 * Parent exception for all custom eu.luminis.elastic based exceptions while using the client.
 */
public class ElasticClientException extends RuntimeException {
    public ElasticClientException(String message) {
        super(message);
    }

    public ElasticClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
