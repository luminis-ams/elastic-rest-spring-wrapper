package eu.luminis.elastic.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Histogram aggregation result.
 */
public class HistogramAggregation extends Aggregation {
    @JsonProperty("buckets")
    private List<HistogramBucket> buckets;

    public List<HistogramBucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<HistogramBucket> buckets) {
        this.buckets = buckets;
    }
}
