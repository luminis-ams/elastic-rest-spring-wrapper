package eu.luminis.elastic.search.response.aggregations;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class AggregationKeyDeserializer extends KeyDeserializer {
    @Override
    public String deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        int position = key.indexOf('#');
        if (position == -1) {
            throw ctxt.mappingException(String.format("Key %s should have structure type#key for aggregations", key));
        }
        return key.substring(position + 1);
    }
}
