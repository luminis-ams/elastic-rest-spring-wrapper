package eu.luminis.elastic.search.response.aggregations.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.luminis.elastic.search.response.aggregations.bucket.BaseBucket;

/**
 * Histogram specific Bucket
 */
public class HistogramBucket extends BaseBucket {

    @JsonProperty(value = "key_as_string")
    private String keyAsString;

    public String getKeyAsString() {
        return keyAsString;
    }

    public void setKeyAsString(String keyAsString) {
        this.keyAsString = keyAsString;
    }
}
