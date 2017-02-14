package eu.luminis.elastic.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.luminis.elastic.document.QueryExecutionException;
import eu.luminis.elastic.search.response.QueryResponse;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static eu.luminis.elastic.helper.AddIdHelper.addIdToEntity;

/**
 * Service that provides methods to execute search requests.
 */
@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    private final RestClient client;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public SearchService(RestClient client, ObjectMapper jacksonObjectMapper) {
        this.client = client;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    /**
     * Executes a search query using the provided template
     * @param request Object containing the required parameters to execute the request
     * @param <T> Type of resulting objects, must be mapped from json result into java entity
     * @return List of mapped objects
     */
    public <T> List<T> queryByTemplate(SearchByTemplateRequest request) {
        Assert.notNull(request, "Need to provide a SearchByTemplateRequest object");

        try {
            Response response = client.performRequest(
                    "GET",
                    request.getIndexName() + "/_search",
                    new HashMap<>(),
                    new StringEntity(request.createQuery(), Charset.defaultCharset()));

            QueryResponse<T> queryResponse = jacksonObjectMapper.readValue(response.getEntity().getContent(), request.getTypeReference());

            List<T> result = new ArrayList<>();
            queryResponse.getHits().getHits().forEach(tHit -> {
                T source = tHit.getSource();
                if (request.getAddId()) {
                    addIdToEntity(tHit.getId(), source);
                }
                result.add(source);
            });

            return result;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a document");
        }

    }


}
