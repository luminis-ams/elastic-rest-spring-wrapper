package eu.luminis.elastic.document;

public class QueryByIdNotFoundException extends RuntimeException {
    private String index;
    private String type;
    private String id;

    public QueryByIdNotFoundException(String index, String type, String id) {
        super("Document with id " + id + ", type " + type + ", index " + index + " could not be found");
        this.id = id;
        this.type = type;
        this.index = index;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
