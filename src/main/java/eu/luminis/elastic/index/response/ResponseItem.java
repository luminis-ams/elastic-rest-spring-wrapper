package eu.luminis.elastic.index.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseItem {
    @JsonProperty(value = "index")
    private IndexResponse index;

    public IndexResponse getIndex() {
        return index;
    }

    public void setIndex(IndexResponse index) {
        this.index = index;
    }
}
