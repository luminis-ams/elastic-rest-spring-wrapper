package eu.luminis.elastic.index;

/**
 * Created by jettrocoenradie on 16/07/16.
 */
public class IndexDocumentException extends RuntimeException {
    public IndexDocumentException(String message) {
        super(message);
    }

    public IndexDocumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
