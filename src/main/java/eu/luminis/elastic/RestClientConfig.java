package eu.luminis.elastic;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.luminis.elastic.search.response.Aggregation;
import eu.luminis.elastic.search.response.AggregationDeserializer;
import eu.luminis.elastic.search.response.Bucket;
import eu.luminis.elastic.search.response.TermsAggregation;
import eu.luminis.elastic.search.response.TermsBucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class
 */
@Configuration
@ComponentScan("eu.luminis.elastic")
public class RestClientConfig {

    public void configObjectMapper(ObjectMapper objectMapper) {
        AggregationDeserializer deserializer = new AggregationDeserializer();
        deserializer.register("byTags", TermsAggregation.class);

        SimpleModule module = new SimpleModule("AggregationDeserializer",
                new Version(1, 0, 0, null, "eu.luminis.elastic", "aggregation-elastic"));
        module.addDeserializer(Aggregation.class, deserializer);

        objectMapper.registerModule(module);
    }
}
