package eu.luminis.elastic.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.luminis.elastic.search.response.aggregations.Aggregation;
import eu.luminis.elastic.search.response.aggregations.AggregationKeyDeserializer;

import java.util.List;
import java.util.Map;

/**
 * Main response class for aggregations response. Can contain hits and aggregations
 */
public class HitsAggsResponse<T> {
    private List<T> hits;

    @JsonProperty
    @JsonDeserialize(keyUsing = AggregationKeyDeserializer.class)
    private Map<String, Aggregation> aggregations;

    private long totalHits;

    public List<T> getHits() {
        return hits;
    }

    public void setHits(List<T> hits) {
        this.hits = hits;
    }

    public Map<String, Aggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(Map<String, Aggregation> aggregations) {
        this.aggregations = aggregations;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }
}
