package eu.luminis.elastic.search.response.aggregations.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.luminis.elastic.search.response.aggregations.bucket.Bucket;

/**
 * Specific bucket implementation for Date Histogram Aggregations.
 */
public class DateHistogramBucket extends Bucket {
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
}
