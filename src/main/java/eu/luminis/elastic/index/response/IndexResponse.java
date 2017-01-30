package eu.luminis.elastic.index.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.luminis.elastic.document.response.Shards;

/**
 * By Jettro Coenradie
 *
 * https://github.com/luminis-ams/elastic-rest-spring-wrapper/blob/master/LICENSE
 */
public class IndexResponse {
    @JsonProperty(value = "_index")
    private String index;

    @JsonProperty(value = "_type")
    private String type;

    @JsonProperty(value = "_id")
    private String id;

    @JsonProperty(value = "_version")
    private long version;

    @JsonProperty(value = "_shards")
    private Shards shards;

    @JsonProperty(value = "result")
    private String result;

    @JsonProperty(value = "created")
    private Boolean created;

    @JsonProperty(value = "error")
    private Error error;

    @JsonProperty(value = "status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }
}
