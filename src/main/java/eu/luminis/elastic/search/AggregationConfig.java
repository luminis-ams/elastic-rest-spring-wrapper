package eu.luminis.elastic.search;

import eu.luminis.elastic.search.response.Aggregation;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration object to tell the ObjectMapper what aggregations it can expect. One should take note not to reuse
 * names of aggregations in case the aggregations are of different type. An example configurtion that you can add to
 * one of the spring config classes is:
 * <pre>
 * &#64;Bean
 * public AggregationConfig aggregationConfig() {
 *   AggregationConfig config = new AggregationConfig();
 *   config.addConfig("byTags", TermsAggregation.class);
 *   config.addConfig("byHistogram", HistogramAggregation.class);
 *   config.addConfig("byDateHistogram", DateHistogramAggregation.class);
 *
 *   return config;
 * }
 * </pre>
 */
public class AggregationConfig {
    private Map<String, Class<? extends Aggregation>> aggregationMap;

    public AggregationConfig() {
        this(new HashMap<>());
    }

    public AggregationConfig(Map<String, Class<? extends Aggregation>> aggregationMap) {
        this.aggregationMap = aggregationMap;
    }

    public Map<String, Class<? extends Aggregation>> getAggregationMap() {
        return aggregationMap;
    }

    public void setAggregationMap(Map<String, Class<? extends Aggregation>> aggregationMap) {
        this.aggregationMap = aggregationMap;
    }

    public AggregationConfig addConfig(String key, Class<? extends Aggregation> clazz) {
        this.getAggregationMap().put(key, clazz);
        return this;
    }
}
