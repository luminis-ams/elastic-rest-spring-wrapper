package eu.luminis.elastic;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.luminis.elastic.search.response.aggregations.Aggregation;
import eu.luminis.elastic.search.response.aggregations.AggregationDeserializer;
import eu.luminis.elastic.search.response.aggregations.AggregationKeyDeserializer;
import eu.luminis.elastic.search.response.aggregations.metric.SingleValueMetricsAggregation;
import eu.luminis.elastic.search.response.aggregations.bucket.DateHistogramAggregation;
import eu.luminis.elastic.search.response.aggregations.bucket.HistogramAggregation;
import eu.luminis.elastic.search.response.aggregations.bucket.TermsAggregation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class
 */
@Configuration
@ComponentScan("eu.luminis.elastic")
public class RestClientConfig {

    @Autowired
    public void configObjectMapper(ObjectMapper objectMapper) {
        AggregationDeserializer deserializer = new AggregationDeserializer();
        deserializer.register("sterms", TermsAggregation.class);
        deserializer.register("histogram", HistogramAggregation.class);
        deserializer.register("date_histogram", DateHistogramAggregation.class);
        deserializer.register("avg", SingleValueMetricsAggregation.class);
        deserializer.register("sum", SingleValueMetricsAggregation.class);
        deserializer.register("max", SingleValueMetricsAggregation.class);
        deserializer.register("min", SingleValueMetricsAggregation.class);
        deserializer.register("cardinality", SingleValueMetricsAggregation.class);
        deserializer.register("value_count", SingleValueMetricsAggregation.class);

        SimpleModule module = new SimpleModule("AggregationDeserializer",
                new Version(1, 0, 0, null, "eu.luminis.elastic", "aggregation-elastic"));
        module.addDeserializer(Aggregation.class, deserializer);
        module.addKeyDeserializer(String.class, new AggregationKeyDeserializer());

        objectMapper.registerModule(module);
    }
}
