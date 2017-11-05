package eu.luminis.elastic.search.response.aggregations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.luminis.elastic.search.response.aggregations.Aggregation;

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
    public Aggregation deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String currentName = jp.getCurrentName();

        int position = currentName.indexOf('#');
        if (position == -1) {
            String message = String.format("Key %s should have structure type#key for aggregations", currentName);
            throw ctxt.mappingException(message);
        }

        String aggType = currentName.substring(0, position);
        String aggKey = currentName.substring(position + 1);

        if (!registry.containsKey(aggType)) {
            String message = String.format("No registered aggregation %s found for polymorphic deserialization", aggType);
            throw ctxt.mappingException(message);
        }

        Class<? extends Aggregation> clazz = registry.get(aggType);
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode obj = mapper.readTree(jp);

        Aggregation aggregation = mapper.treeToValue(obj, clazz);
        aggregation.setName(aggKey);
        return aggregation;
    }
}
