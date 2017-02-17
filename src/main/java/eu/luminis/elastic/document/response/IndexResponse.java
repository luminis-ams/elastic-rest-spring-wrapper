package eu.luminis.elastic.document.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexResponse {
    @JsonProperty(value = "_index")
    private String index;

    @JsonProperty(value = "_type")
    private String type;

    @JsonProperty(value = "_id")
    private String id;

    @JsonProperty(value = "_version")
    private long version;

    @JsonProperty(value = "result")
    private String result;

    @JsonProperty(value = "created")
    private Boolean created;

    @JsonProperty(value = "_shards")
    private Shards shards;

    @JsonProperty(value = "forced_refresh")
    private Boolean forcedRefresh;

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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public Boolean getForcedRefresh() {
        return forcedRefresh;
    }

    public void setForcedRefresh(Boolean forcedRefresh) {
        this.forcedRefresh = forcedRefresh;
    }
}
