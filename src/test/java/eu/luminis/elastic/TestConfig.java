package eu.luminis.elastic;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.luminis.elastic.search.AggregationConfig;
import eu.luminis.elastic.search.response.Aggregation;
import eu.luminis.elastic.search.response.AggregationDeserializer;
import eu.luminis.elastic.search.response.TermsAggregation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;

@Configuration
@PropertySource("classpath:test.properties")
public class TestConfig {

    @Bean
    public AggregationConfig aggregationConfig() {
        AggregationConfig config = new AggregationConfig();
        config.addConfig("byTags", TermsAggregation.class);

        return config;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public IndexDocumentHelper indexDocumentHelper() {
        return new IndexDocumentHelper();
    }
}
