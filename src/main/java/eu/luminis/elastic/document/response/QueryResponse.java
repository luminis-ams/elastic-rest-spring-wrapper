package eu.luminis.elastic.document.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class QueryResponse<T> {
    private Long took;

    @JsonProperty(value = "timed_out")
    private Boolean timedOut;

    @JsonProperty(value = "_shards")
    private Shards shards;

    private Hits<T> hits;

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
}
