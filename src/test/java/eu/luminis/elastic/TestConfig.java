package eu.luminis.elastic;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.luminis.elastic.search.response.Aggregation;
import eu.luminis.elastic.search.response.AggregationDeserializer;
import eu.luminis.elastic.search.response.TermsAggregation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:test.properties")
public class TestConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        AggregationDeserializer deserializer = new AggregationDeserializer();
        deserializer.register("byTags", TermsAggregation.class);

        SimpleModule module = new SimpleModule("AggregationDeserializer",
                new Version(1, 0, 0, null, "eu.luminis.elastic", "aggregation-elastic"));
        module.addDeserializer(Aggregation.class, deserializer);

        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public IndexDocumentHelper indexDocumentHelper() {
        return new IndexDocumentHelper();
    }
}
