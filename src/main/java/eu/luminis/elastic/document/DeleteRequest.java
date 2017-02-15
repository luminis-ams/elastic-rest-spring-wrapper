package eu.luminis.elastic.document;

/**
 * Request used to provide the parameters that are required to remove a document.
 */
public class DeleteRequest extends DocumentRequest {
    public DeleteRequest() {
    }

    public DeleteRequest(String index, String type, String id) {
        super(index, type, id);
    }
}
