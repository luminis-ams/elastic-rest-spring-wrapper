package eu.luminis.elastic.document;

/**
 * Specific request to handle updates to documents. Only partial updates are supported at the moment.
 */
public class UpdateRequest extends DocumentRequest {
    private Object entity;

    public UpdateRequest(String index, String type, String id) {
        super(index, type, id);
    }

    public Object getEntity() {
        return entity;
    }

    public UpdateRequest setEntity(Object entity) {
        this.entity = entity;
        return this;
    }
}
