package eu.luminis.elastic.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.luminis.elastic.document.response.GetByIdResponse;
import eu.luminis.elastic.document.response.IndexResponse;
import eu.luminis.elastic.index.IndexDocumentException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static eu.luminis.elastic.helper.AddIdHelper.addIdToEntity;

/**
 * Used to execute actions that interact with the documents in an elasticsearch index.
 */
@Component
public class DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final RestClient client;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public DocumentService(RestClient client, ObjectMapper jacksonObjectMapper) {
        this.client = client;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    /**
     * By specifying the unique identification of an object we can return only that object. If we cannot find the object
     * we throw an {@link QueryByIdNotFoundException}.
     *
     * @param request Object containing the required parameters
     * @param <T>     Type of the object to be mapped to
     * @return Found object of type T
     */
    public <T> T queryById(QueryByIdRequest request) {
        try {
            String endpoint = String.format("/%s/%s/%s", request.getIndex(), request.getType(), request.getId());

            Response response = client.performRequest("GET", endpoint, getRequestParams(request));

            GetByIdResponse<T> queryResponse = jacksonObjectMapper.readValue(response.getEntity().getContent(), request.getTypeReference());

            if (!queryResponse.getFound()) {
                throw new QueryByIdNotFoundException(request.getIndex(), request.getType(), request.getId());
            }

            T entity = queryResponse.getSource();

            if (request.getAddId()) {
                addIdToEntity(request.getId(), entity);
            }

            return entity;
        } catch (ResponseException re) {
            if (re.getResponse().getStatusLine().getStatusCode() == 404) {
                throw new QueryByIdNotFoundException(request.getIndex(), request.getType(), request.getId());
            } else {
                logger.warn("Problem while executing request.", re);
                throw new QueryExecutionException("Error when executing a document");
            }
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a document");
        }
    }

    /**
     * Index the provided document using the provided parameters. If an id is provided we do an update, of no id is
     * provided we do an inset and we return the id.
     *
     * @param request Object containing the required parameters
     * @return Generated ID
     */
    public String index(IndexRequest request) {
        try {
            HttpEntity requestBody = new StringEntity(jacksonObjectMapper.writeValueAsString(request.getEntity()), Charset.defaultCharset());

            String method;
            String endpoint;
            if (request.getId() != null) {
                method = "PUT";
                endpoint = String.format("/%s/%s/%s", request.getIndex(), request.getType(), request.getId());
            } else {
                method = "POST";
                endpoint = String.format("/%s/%s", request.getIndex(), request.getType());
            }
            Response response = client.performRequest(
                    method,
                    endpoint,
                    getRequestParams(request),
                    requestBody);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                logger.warn("Problem while indexing a document: {}", response.getStatusLine().getReasonPhrase());
                throw new QueryExecutionException("Could not index a document, status code is " + statusCode);
            }

            IndexResponse queryResponse = jacksonObjectMapper.readValue(response.getEntity().getContent(), IndexResponse.class);

            return queryResponse.getId();
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new IndexDocumentException("Error when executing a document");
        }

    }

    /**
     * Removes the document with the provided unique identification.
     *
     * @param request Request object contaning the required parameters
     * @return Message line that can be used to see if we succeeded.
     */
    public String remove(DeleteRequest request) {
        try {
            String endpoint = String.format("/%s/%s/%s", request.getIndex(), request.getType(), request.getId());
            Response response = client.performRequest("DELETE", endpoint);

            return response.getStatusLine().getReasonPhrase();
        } catch (IOException e) {
            logger.warn("Problem while removing a document.", e);
            throw new IndexDocumentException("Error when removing a document");
        }
    }

    private Map<String, String> getRequestParams(DocumentRequest request) {
        Map<String, String> params = new HashMap<>();
        if (request.getRefresh() != null && !request.getRefresh().equals(Refresh.NONE)) {
            params.put("refresh", request.getRefresh().getName());
        }
        return params;
    }

}
