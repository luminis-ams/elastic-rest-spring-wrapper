package eu.luminis.elastic.index.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.gerimedica.ysis.search.elastic.document.response.Shards;

public class CountResponse {

    @JsonProperty(value = "count")
    private long count;

    @JsonProperty(value = "_shards")
    private Shards shards;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }
}
