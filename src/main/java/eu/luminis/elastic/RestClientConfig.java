package eu.luminis.elastic;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.luminis.elastic.search.AggregationConfig;
import eu.luminis.elastic.search.response.Aggregation;
import eu.luminis.elastic.search.response.AggregationDeserializer;
import eu.luminis.elastic.search.response.Bucket;
import eu.luminis.elastic.search.response.TermsAggregation;
import eu.luminis.elastic.search.response.TermsBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * Configuration class
 */
@Configuration
@ComponentScan("eu.luminis.elastic")
public class RestClientConfig {

    @Autowired
    public void configObjectMapper(ObjectMapper objectMapper, Optional<AggregationConfig> aggregationConfig) {
        AggregationDeserializer deserializer = new AggregationDeserializer();

        aggregationConfig.ifPresent(config -> {
            config.getAggregationMap().forEach(deserializer::register);
        });

        SimpleModule module = new SimpleModule("AggregationDeserializer",
                new Version(1, 0, 0, null, "eu.luminis.elastic", "aggregation-elastic"));
        module.addDeserializer(Aggregation.class, deserializer);

        objectMapper.registerModule(module);
    }
}
