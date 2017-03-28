package eu.luminis.elastic.search;

import eu.luminis.elastic.search.response.Aggregation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jettrocoenradie on 28/03/2017.
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
