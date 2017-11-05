package eu.luminis.elastic.search.response.aggregations.bucket;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.luminis.elastic.search.response.aggregations.Aggregation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jettrocoenradie on 15/04/2017.
 */
public class BaseBucket extends Bucket {
    @JsonProperty("key")
    private String key;

    @JsonProperty("doc_count")
    private Long docCount;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

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
    public Map<String, Aggregation> getProperties() {
        return aggregations;
    }

    private Map<String, Aggregation> aggregations = new HashMap<>();

    public Map<String, Aggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(Map<String, Aggregation> aggregations) {
        this.aggregations = aggregations;
    }

}
