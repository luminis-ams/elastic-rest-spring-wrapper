package eu.luminis.elastic.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Date Histogram representation of an aggregation.
 */
public class DateHistogramAggregation extends Aggregation {
    @JsonProperty("buckets")
    private List<DateHistogramBucket> buckets;

    public List<DateHistogramBucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<DateHistogramBucket> buckets) {
        this.buckets = buckets;
    }
}
