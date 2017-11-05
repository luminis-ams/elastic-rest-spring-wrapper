package eu.luminis.elastic.search.response.aggregations.metric;

import eu.luminis.elastic.search.response.aggregations.Aggregation;

/**
 * Specific metric aggregation that can be used by: avg, max, min, etc. All aggregations that return just one value.
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
