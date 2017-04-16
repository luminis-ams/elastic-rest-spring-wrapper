package eu.luminis.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.luminis.elastic.search.AggregationConfig;
import eu.luminis.elastic.search.response.DateHistogramAggregation;
import eu.luminis.elastic.search.response.HistogramAggregation;
import eu.luminis.elastic.search.response.TermsAggregation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:test.properties")
public class TestConfig {

    @Bean
    public AggregationConfig aggregationConfig() {
        AggregationConfig config = new AggregationConfig();
        config.addConfig("byTags", TermsAggregation.class);
        config.addConfig("byHistogram", HistogramAggregation.class);
        config.addConfig("byDateHistogram", DateHistogramAggregation.class);

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
