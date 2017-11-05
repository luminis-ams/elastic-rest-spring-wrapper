package eu.luminis.elastic.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.luminis.elastic.document.response.Hits;
import eu.luminis.elastic.document.response.Shards;
import eu.luminis.elastic.search.response.aggregations.Aggregation;

import java.util.Map;


public class QueryResponse<T> {
    private Long took;

    @JsonProperty(value = "timed_out")
    private Boolean timedOut;

    @JsonProperty(value = "_shards")
    private Shards shards;

    private Hits<T> hits;

    private Map<String,Aggregation> aggregations;

    /* Getters / Setters */
    public Hits<T> getHits() {
        return hits;
    }

    public void setHits(Hits<T> hits) {
        this.hits = hits;
    }

    public Long getTook() {
        return took;
    }

    public void setTook(Long took) {
        this.took = took;
    }

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public Boolean getTimedOut() {
        return timedOut;
    }

    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }

    public Map<String, Aggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(Map<String, Aggregation> aggregations) {
        this.aggregations = aggregations;
    }
}
