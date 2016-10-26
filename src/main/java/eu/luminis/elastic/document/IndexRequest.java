package eu.luminis.elastic.document;

/**
 * Created by jettrocoenradie on 23/08/2016.
 */
public class IndexRequest {
    private String index;
    private String type;
    private String id;
    private Object entity;
    private Boolean addId;

    public Boolean getAddId() {
        return addId;
    }

    public IndexRequest setAddId(Boolean addId) {
        this.addId = addId;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public IndexRequest setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return type;
    }

    public IndexRequest setType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public IndexRequest setId(String id) {
        this.id = id;
        return this;
    }

    public Object getEntity() {
        return entity;
    }

    public IndexRequest setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    public static IndexRequest create() {
        return new IndexRequest();
    }
}
