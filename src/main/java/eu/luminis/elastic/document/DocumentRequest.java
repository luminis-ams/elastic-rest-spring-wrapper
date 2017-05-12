package eu.luminis.elastic.document;

public class DocumentRequest {
    private String index;
    private String type;
    private String id;
    private Refresh refresh;

    public DocumentRequest() {
    }

    public DocumentRequest(String index, String type, String id) {
        this.index = index;
        this.type = type;
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public DocumentRequest setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return type;
    }

    public DocumentRequest setType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public DocumentRequest setId(String id) {
        this.id = id;
        return this;
    }

    public Refresh getRefresh() {
        return refresh;
    }

    public DocumentRequest setRefresh(Refresh refresh) {
        this.refresh = refresh;
        return this;
    }
}
