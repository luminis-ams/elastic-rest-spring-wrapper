package eu.luminis.elastic.search.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AggregationDeserializer extends StdDeserializer<Aggregation> {

    private Map<String, Class<? extends Aggregation>> registry;

    public AggregationDeserializer() {
        super(Aggregation.class);
        registry = new HashMap<>();
    }

    public void register(String aggName, Class<? extends Aggregation> clazz) {
        registry.put(aggName, clazz);
    }

    @Override
    public Aggregation deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String currentName = jp.getCurrentName();
        if (!registry.containsKey(currentName)) {
            throw ctxt.mappingException("No registered aggregation name found for polymorphic deserialization");
        }

        Class<? extends Aggregation> clazz = registry.get(currentName);
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode obj = mapper.readTree(jp);

        return mapper.treeToValue(obj, clazz);
    }
}
