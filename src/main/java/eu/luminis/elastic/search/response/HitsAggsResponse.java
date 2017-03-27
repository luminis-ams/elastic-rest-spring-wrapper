package eu.luminis.elastic.search.response;

import java.util.List;
import java.util.Map;

/**
 * Created by jettrocoenradie on 27/03/2017.
 */
public class HitsAggsResponse<T> {
    private List<T> hits;
    private Map<String, Aggregation> aggregations;

    public List<T> getHits() {
        return hits;
    }

    public void setHits(List<T> hits) {
        this.hits = hits;
    }

    public Map<String, Aggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(Map<String, Aggregation> aggregations) {
        this.aggregations = aggregations;
    }
}
