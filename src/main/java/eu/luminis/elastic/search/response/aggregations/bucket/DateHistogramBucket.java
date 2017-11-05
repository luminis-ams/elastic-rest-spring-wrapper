package eu.luminis.elastic.search.response.aggregations.bucket;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.luminis.elastic.search.response.aggregations.Aggregation;
import eu.luminis.elastic.search.response.aggregations.bucket.Bucket;

import java.util.HashMap;
import java.util.Map;

/**
 * Specific bucket implementation for Date Histogram Aggregations.
 */
public class DateHistogramBucket implements Bucket {
    @JsonProperty("key_as_string")
    private String keyAsString;

    private Long key;

    @JsonProperty("doc_count")
    private Long docCount;

    public String getKeyAsString() {
        return keyAsString;
    }

    public void setKeyAsString(String keyAsString) {
        this.keyAsString = keyAsString;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    private Map<String, Aggregation> aggregations = new HashMap<>();

    @JsonAnySetter
    public void add(String key, Aggregation aggregation) {
        int position = key.indexOf('#');
        if (position != -1) {
            aggregations.put(key.substring(position + 1), aggregation);
        } else {
            aggregations.put(key, aggregation);
        }
    }

    @JsonAnyGetter
    public Map<String, Aggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(Map<String, Aggregation> aggregations) {
        this.aggregations = aggregations;
    }

}
