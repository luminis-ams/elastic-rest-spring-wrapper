package eu.luminis.elastic.search.response.aggregations.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.luminis.elastic.search.response.aggregations.Aggregation;

import java.util.List;

/**
 * Terms aggregation specific response
 */
public class TermsAggregation extends Aggregation {
    @JsonProperty("doc_count_error_upper_bound")
    private Long docCountErrorUpperBound;

    @JsonProperty("sum_other_doc_count")
    private Long sumOtherDocCount;

    @JsonProperty("buckets")
    private List<TermsBucket> buckets;

    public Long getDocCountErrorUpperBound() {
        return docCountErrorUpperBound;
    }

    public void setDocCountErrorUpperBound(Long docCountErrorUpperBound) {
        this.docCountErrorUpperBound = docCountErrorUpperBound;
    }

    public Long getSumOtherDocCount() {
        return sumOtherDocCount;
    }

    public void setSumOtherDocCount(Long sumOtherDocCount) {
        this.sumOtherDocCount = sumOtherDocCount;
    }

    public List<TermsBucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<TermsBucket> buckets) {
        this.buckets = buckets;
    }
}
