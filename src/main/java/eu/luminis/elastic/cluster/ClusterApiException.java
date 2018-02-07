package eu.luminis.elastic.cluster;

import eu.luminis.elastic.ElasticClientException;

/**
 * Exception thrown while interacting with the cluster api.
 */
public class ClusterApiException extends ElasticClientException {
    /**
     * Constructor used when throwing this exception without catching another exception first
     * @param message Descriptive exception that might me used by the client
     */
    public ClusterApiException(String message) {
        super(message);
    }

    /**
     * Constructor used when throwing this exception after catching another exception
     * @param message String with a description of the reason to throw this exception
     * @param cause The actual error that was caught before this exception
     */
    public ClusterApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
