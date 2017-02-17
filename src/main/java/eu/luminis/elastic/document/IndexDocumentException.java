package eu.luminis.elastic.document;

public class IndexDocumentException extends RuntimeException {
    public IndexDocumentException(String message) {
        super(message);
    }

    public IndexDocumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
