package eu.luminis.elastic.index.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BulkIndexResponse {
    @JsonProperty(value = "errors")
    private Boolean errors;

    @JsonProperty(value = "items")
    private List<ResponseItem> items;

    @JsonProperty(value = "took")
    private Long took;

    public Boolean getErrors() {
        return errors;
    }

    public void setErrors(Boolean errors) {
        this.errors = errors;
    }

    public List<ResponseItem> getItems() {
        return items;
    }

    public void setItems(List<ResponseItem> items) {
        this.items = items;
    }

    public Long getTook() {
        return took;
    }

    public void setTook(Long took) {
        this.took = took;
    }
}
