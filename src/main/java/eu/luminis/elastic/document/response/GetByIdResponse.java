package eu.luminis.elastic.document.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetByIdResponse<T> {
    @JsonProperty(value = "_index")
    private String index;

    @JsonProperty(value = "_type")
    private String type;

    @JsonProperty(value = "_id")
    private String id;

    @JsonProperty(value = "found")
    private Boolean found;

    @JsonProperty(value = "_source")
    private T source;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}
