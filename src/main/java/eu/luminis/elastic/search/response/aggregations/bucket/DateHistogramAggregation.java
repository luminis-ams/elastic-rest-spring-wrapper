package eu.luminis.elastic.search.response.aggregations.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.luminis.elastic.search.response.aggregations.Aggregation;

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
