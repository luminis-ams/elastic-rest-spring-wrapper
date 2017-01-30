package eu.luminis.elastic.index;

import eu.luminis.elastic.ElasticRestResponseException;
import org.apache.http.StatusLine;

/**
 * Created by jettrocoenradie on 16/07/16.
 */
public class IndexDocumentException extends ElasticRestResponseException {

    public IndexDocumentException(StatusLine statusLine) {
        super(statusLine);
    }

    public IndexDocumentException(String message) {
        super(message);
    }

    public IndexDocumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
