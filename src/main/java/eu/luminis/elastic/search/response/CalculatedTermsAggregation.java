package eu.luminis.elastic.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by jettrocoenradie on 19/05/2017.
 */
public class CalculatedTermsAggregation extends Aggregation {
    @JsonProperty("doc_count_error_upper_bound")
    private Long docCountErrorUpperBound;

    @JsonProperty("sum_other_doc_count")
    private Long sumOtherDocCount;

    @JsonProperty("buckets")
    private List<CalculatedTermsBucket> buckets;

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

    public List<CalculatedTermsBucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<CalculatedTermsBucket> buckets) {
        this.buckets = buckets;
    }

}
