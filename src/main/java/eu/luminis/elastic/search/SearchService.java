package eu.luminis.elastic.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.luminis.elastic.document.QueryExecutionException;
import eu.luminis.elastic.search.response.HitsResponse;
import eu.luminis.elastic.search.response.aggregations.metric.MetricResponse;
import eu.luminis.elastic.search.response.HitsAggsResponse;
import eu.luminis.elastic.search.response.query.ElasticQueryResponse;
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
import java.util.Map;

import static eu.luminis.elastic.RequestMethod.GET;
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
     *
     * @param request Object containing the required parameters to execute the request
     * @param <T>     Type of resulting objects, must be mapped from json result into java entity
     * @return List of mapped objects
     */
    public <T> HitsResponse<T> queryByTemplate(SearchByTemplateRequest request) {
        Assert.notNull(request, "Need to provide a SearchByTemplateRequest object");

        try {
            ElasticQueryResponse<T> elasticQueryResponse = doExecuteQuery(request);
            HitsResponse<T> hitsResponse = new HitsResponse<>();

            putInfoFromQueryIntoHitsResponse(request, elasticQueryResponse, hitsResponse);

            return hitsResponse;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a document");
        }
    }

    /**
     * Executes a search request with the provided query, but expects an aggregation part in the query. It will not
     * fail in case you do not provide an aggregation.
     *
     * @param request Object containing the required parameters to execute the request
     * @param <T>     Type of resulting objects, must be mapped from json result into java entity
     * @return Object containing the list of objects and/or the aggregations
     */
    public <T> HitsAggsResponse<T> aggsByTemplate(SearchByTemplateRequest request) {
        Assert.notNull(request, "Need to provide a SearchByTemplateRequest object");

        try {
            ElasticQueryResponse<T> elasticQueryResponse = doExecuteQuery(request);
            HitsAggsResponse<T> hitsAggsResponse = new HitsAggsResponse<>();
            putInfoFromQueryIntoHitsResponse(request,elasticQueryResponse,hitsAggsResponse);

            hitsAggsResponse.setAggregations(elasticQueryResponse.getAggregations());
            return hitsAggsResponse;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a document");
        }


    }

    /**
     * Returns the number of documents in the specified index
     *
     * @param indexName The name of the index to use for counting documents
     * @return Long representing the number of documents in the provided index.
     */
    public Long countByIndex(String indexName) {
        try {
            Response response = client.performRequest(GET, indexName + "/_count");

            return jacksonObjectMapper.readValue(response.getEntity().getContent(), MetricResponse.class).getCount();

        } catch (IOException e) {
            logger.warn("Problem while executing count request.", e);
            throw new QueryExecutionException("Error when executing count request");
        }

    }

    private <T> ElasticQueryResponse<T> doExecuteQuery(SearchByTemplateRequest request) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("typed_keys", null);
        Response response = client.performRequest(
                GET,
                request.getIndexName() + "/_search",
                params,
                new StringEntity(request.createQuery(), Charset.defaultCharset()));

        return jacksonObjectMapper.readValue(response.getEntity().getContent(), request.getTypeReference());
    }

    private <T> void putInfoFromQueryIntoHitsResponse(SearchByTemplateRequest request, ElasticQueryResponse<T> elasticQueryResponse, HitsResponse<T> hitsResponse) {
        List<T> hits = extractHitsByType(request, elasticQueryResponse);
        hitsResponse.setHits(hits);
        hitsResponse.setTotalHits(elasticQueryResponse.getHits().getTotal());
        hitsResponse.setTimedOut(elasticQueryResponse.getTimedOut());
        hitsResponse.setResponseTime(elasticQueryResponse.getTook());
    }

    private <T> List<T> extractHitsByType(SearchByTemplateRequest request, ElasticQueryResponse<T> elasticQueryResponse) {
        List<T> hits = new ArrayList<>();
        elasticQueryResponse.getHits().getHits().forEach(tHit -> {
            T source = tHit.getSource();
            if (request.getAddId()) {
                addIdToEntity(tHit.getId(), source);
            }
            hits.add(source);
        });
        return hits;
    }
}
