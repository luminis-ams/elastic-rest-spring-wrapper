package eu.luminis.elastic.search.response.aggregations.metric;

import eu.luminis.elastic.search.response.aggregations.Aggregation;

/**
 * Created by jettrocoenradie on 19/05/2017.
 */
public class SingleValueMetricsAggregation extends Aggregation {
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
