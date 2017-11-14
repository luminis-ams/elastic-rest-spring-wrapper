package eu.luminis.elastic.document;

/**
 * Request used to check if a document exists.
 */
public class ExistsRequest extends DocumentRequest {
    public ExistsRequest(String index, String type, String id) {
        super(index, type, id);
    }
}
