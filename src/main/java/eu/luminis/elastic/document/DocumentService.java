package eu.luminis.elastic.document;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.luminis.elastic.cluster.ClusterManagementService;
import eu.luminis.elastic.document.response.GetByIdResponse;
import eu.luminis.elastic.document.response.IndexResponse;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static eu.luminis.elastic.RequestMethod.DELETE;
import static eu.luminis.elastic.RequestMethod.GET;
import static eu.luminis.elastic.RequestMethod.HEAD;
import static eu.luminis.elastic.RequestMethod.POST;
import static eu.luminis.elastic.RequestMethod.PUT;
import static eu.luminis.elastic.helper.AddIdHelper.addIdToEntity;

/**
 * Used to execute actions that interact with the documents in an elasticsearch index.
 */
@Component
public class DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final ClusterManagementService clusterManagementService;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public DocumentService(ClusterManagementService clusterManagementService, ObjectMapper jacksonObjectMapper) {
        this.clusterManagementService = clusterManagementService;
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
        if (request.getTypeReference() == null) {
            throw new QueryExecutionException("The TypeReference in the request cannot be null");
        }
        try {
            String endpoint = createEndpointString(request.getIndex(), request.getType(), request.getId());

            Response response = clusterManagementService.getCurrentClient().performRequest(GET, endpoint, getRequestParams(request));

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
     * Checks if the provided document exists or not. If we can find the document true is returned, else false
     *
     * @param request ExistsRequest that must contain an index, type and id to check if the document exists
     * @return Boolean true if the document exists, false otherwise
     */
    public Boolean exists(ExistsRequest request) {
        try {
            String endpoint = createEndpointString(request.getIndex(), request.getType(), request.getId());
            Response response = clusterManagementService.getCurrentClient().performRequest(HEAD, endpoint);

            int statusCode = response.getStatusLine().getStatusCode();

            return statusCode == 200;
        } catch (IOException e) {
            logger.warn("Problem while removing a document.", e);
            throw new IndexDocumentException("Error when removing a document");
        }

    }

    /**
     * Index the provided document using the provided parameters. If an id is provided we do an update, of no id is
     * provided we do an insert and we return the id.
     *
     * @param request Object containing the required parameters
     * @return Generated ID
     */
    public String index(IndexRequest request) {
        String method;
        String endpoint;
        if (request.getId() != null) {
            method = PUT;
            endpoint = createEndpointString(request.getIndex(), request.getType(), request.getId());
        } else {
            method = POST;
            endpoint = createGenerateIdEndpointString(request.getIndex(), request.getType());
        }

        return doIndex(request, method, endpoint);
    }

    /**
     * Throws an exception if the provided request contains an id that already exists.
     *
     * @param request IndexRequest containing the entity to index
     * @return The id of the object, which is the same as the one you have provided.
     */
    public String create(IndexRequest request) {
        if (request.getId() == null) {
            throw new QueryExecutionException("Executing create request without an id");
        }
        String endpoint = createEndpointCreateString(request.getIndex(), request.getType(), request.getId());
        return doIndex(request, PUT, endpoint);
    }

    /**
     * Removes the document with the provided unique identification.
     *
     * @param request Request object containing the required parameters
     * @return Message line that can be used to see if we succeeded.
     */
    public String remove(DeleteRequest request) {
        try {
            String endpoint = createEndpointString(request.getIndex(), request.getType(), request.getId());
            if (!request.isMustExist() &&
                    !exists(new ExistsRequest(request.getIndex(), request.getType(), request.getId()))) {
                return "not_exists";
            }
            Response response = clusterManagementService.getCurrentClient().performRequest(DELETE, endpoint);

            return response.getStatusLine().getReasonPhrase();
        } catch (IOException e) {
            logger.warn("Problem while removing a document.", e);
            throw new IndexDocumentException("Error when removing a document");
        }
    }

    /**
     * Updates the document as specified in the provided UpdateRequest.
     *
     * @param request UpdateRequest containing the required information to execute an update
     * @return
     */
    public String update(UpdateRequest request) {
        String endpoint = createEndpointUpdateString(request.getIndex(), request.getType(), request.getId());
        WrappedEntity entity = new WrappedEntity(request.getEntity());
        try {
            HttpEntity requestBody = new StringEntity(jacksonObjectMapper.writeValueAsString(entity), Charset.defaultCharset());
            Response response = clusterManagementService.getCurrentClient().performRequest(
                    POST,
                    endpoint,
                    getRequestParams(request),
                    requestBody);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                logger.warn("Problem while updating a document: {}", response.getStatusLine().getReasonPhrase());
                throw new IndexDocumentException("Could not update a document, status code is " + statusCode);
            }

            IndexResponse queryResponse = jacksonObjectMapper.readValue(response.getEntity().getContent(), IndexResponse.class);

            return queryResponse.getId();
        } catch (IOException e) {
            logger.warn("Problem while executing request to update a document.", e);
            throw new IndexDocumentException("Error when updating a document");
        }

    }

    private String doIndex(IndexRequest request, String method, String endpoint) {
        try {
            HttpEntity requestBody = new StringEntity(jacksonObjectMapper.writeValueAsString(request.getEntity()), Charset.defaultCharset());
            Response response = clusterManagementService.getCurrentClient().performRequest(
                    method,
                    endpoint,
                    getRequestParams(request),
                    requestBody);

            IndexResponse queryResponse = jacksonObjectMapper.readValue(response.getEntity().getContent(), IndexResponse.class);

            return queryResponse.getId();
        } catch (ResponseException e) {
            Response response = e.getResponse();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 409) {
                logger.warn("version conflict, trying to create an existing document?");
                throw new IndexDocumentException("Document already exists");
            } else {
                logger.warn("Problem while indexing a document: {}", response.getStatusLine().getReasonPhrase());
                throw new IndexDocumentException("Could not index a document, status code is " + statusCode);
            }
        } catch (IOException e) {
            logger.warn("Problem while executing request to index a document.", e);
            throw new IndexDocumentException("Error when indexing a document");
        }
    }

    private Map<String, String> getRequestParams(DocumentRequest request) {
        Map<String, String> params = new HashMap<>();
        if (request.getRefresh() != null && !request.getRefresh().equals(Refresh.NONE)) {
            params.put("refresh", request.getRefresh().getName());
        }
        return params;
    }

    private String createEndpointString(String index, String type, String id) {
        return String.format("/%s/%s/%s", index, type, id);
    }

    private String createGenerateIdEndpointString(String index, String type) {
        return String.format("/%s/%s", index, type);
    }

    private String createEndpointCreateString(String index, String type, String id) {
        return String.format("/%s/%s/%s/_create", index, type, id);
    }

    private String createEndpointUpdateString(String index, String type, String id) {
        return String.format("/%s/%s/%s/_update", index, type, id);
    }

}
