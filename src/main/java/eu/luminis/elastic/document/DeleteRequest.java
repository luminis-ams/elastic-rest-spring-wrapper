package eu.luminis.elastic.document;

/**
 * Request used to provide the parameters that are required to remove a document.
 */
public class DeleteRequest extends DocumentRequest {
    private boolean mustExist = false;

    public DeleteRequest(String index, String type, String id) {
        super(index, type, id);
    }

    public boolean isMustExist() {
        return mustExist;
    }

    public void setMustExist(boolean mustExist) {
        this.mustExist = mustExist;
    }
}
