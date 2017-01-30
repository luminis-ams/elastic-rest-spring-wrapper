package eu.luminis.elastic.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.luminis.elastic.document.response.GetByIdResponse;
import eu.luminis.elastic.document.response.QueryResponse;
import eu.luminis.elastic.index.IndexDocumentException;
import eu.luminis.elastic.index.response.BulkIndexResponse;
import eu.luminis.elastic.index.response.IndexResponse;
import org.apache.commons.collections4.ListUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static eu.luminis.elastic.document.IndexRequest.Action.DELETE;
import static eu.luminis.elastic.document.IndexRequest.Action.UPDATE;
import static java.util.stream.Collectors.joining;

/**
 * Used to execute actions that interact with the documents in an elasticsearch index.
 */
@Component
public class DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final RestClient client;
    private final ObjectMapper jacksonObjectMapper;
    public static final int BATCH_SIZE = 1000;

    @Autowired
    public DocumentService(RestClient client, ObjectMapper jacksonObjectMapper) {
        this.client = client;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    /**
     * Executes a search query using the provided template
     * @param request Object containing the required parameters to execute the request
     * @param <T> Type of resulting objects, must be mapped from json result into java entity
     * @return List of mapped objects
     */
    public <T> List<T> queryByTemplate(QueryByTemplateRequest request) {
        Assert.notNull(request, "Need to provide a QueryByTemplateRequest object");

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

    /**
     * By specifying the unique identification of an object we can return only that object. If we cannot find the object
     * we throw an {@link QueryByIdNotFoundException}.
     * @param request Object containing the required parameters
     * @param <T> Type of the object to be mapped to
     * @return Found object of type T
     */
    public <T> T querybyId(QueryByIdRequest request) {
        try {
            Response response = client.performRequest(
                    "GET",
                    "/" + request.getIndex() + "/" + request.getType() + "/" + request.getId());

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
                throw new QueryByIdNotFoundException(request.getIndex(),request.getType(),request.getId());
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
     * @param indexRequest Object containing the required parameters
     * @return Generated ID
     */
    public String index(IndexRequest indexRequest) {
        try {
            HttpEntity requestBody = new StringEntity(jacksonObjectMapper.writeValueAsString(indexRequest.getEntity()), Charset.defaultCharset());

            Response response;
            if (indexRequest.getId() != null) {
                response = client.performRequest(
                        "PUT",
                        indexRequest.getIndex() + "/" + indexRequest.getType() + "/" + indexRequest.getId(),
                        new Hashtable<>(),
                        requestBody);
            } else {
                response = client.performRequest(
                        "POST",
                        indexRequest.getIndex() + "/" + indexRequest.getType(),
                        new Hashtable<>(),
                        requestBody);
            }

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
     * @param index String containing the index part
     * @param type String contaning the type part
     * @param id String containing the id part
     * @return Message line that can be used to see if we succeeded.
     */
    public String remove(String index, String type, String id) {
        try {
            Response response = client.performRequest(
                    "DELETE",
                    index + "/" + type + "/" + id,
                    new Hashtable<>());
            return response.getStatusLine().getReasonPhrase();
        } catch (IOException e) {
            logger.warn("Problem while removing a document.", e);
            throw new IndexDocumentException("Error when removing a document");
        }
    }


    private String makeIndexLine(IndexRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("_index", request.getIndex());
        map.put("_type", request.getType());
        if (request.getId() != null) {
            map.put("_id", request.getId());
        }
        Map<String, Object> command = new HashMap<>();
        command.put(request.action(), map);
        try {
            return jacksonObjectMapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String bulkUpdateToJson(List<IndexRequest> requests) {
        return requests.stream().map(req -> {
            try {
                String commandStr = makeIndexLine(req);
                if (req.getAction() == DELETE) {
                    return commandStr;
                } else {
                    Object entity;
                    if (req.getAction() == UPDATE) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("doc", req.getEntity());
                        entity = map;
                    } else {
                        entity = req.getEntity();
                    }
                    return commandStr + "\n" + jacksonObjectMapper.writeValueAsString(entity);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(joining("\n")) + "\n"; // _bulk update request body must be terminated by new line
    }

    /**
     * @param indexRequests
     * @return number of documents successfully indexed
     */
    public long bulkIndex(List<IndexRequest> indexRequests) {
        final AtomicLong count = new AtomicLong(0);
        ListUtils.partition(indexRequests, BATCH_SIZE).forEach(requestChunk -> {
            int errors = bulkIndex(bulkUpdateToJson(requestChunk));
            count.addAndGet(requestChunk.size() - errors);
        });

        logger.debug("Indexed documents {} / {}", count.longValue(), indexRequests.size());

        return count.get();
    }

    /**
     * @return true if errors
     */
    private int bulkIndex(String request) {
        try {
            HttpEntity requestEntity = new StringEntity(request, Charset.forName("UTF-8"));
            Response response = client.performRequest("POST","_bulk", new HashMap<>(), requestEntity);
            EntityUtils.consume(requestEntity);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                logger.warn("Problem while indexing a document: {}", response.getStatusLine().getReasonPhrase());
                throw new QueryExecutionException("Could not index a document, status code is " + statusCode);
            }

            BulkIndexResponse queryResponse = jacksonObjectMapper.readValue(response.getEntity().getContent(), BulkIndexResponse.class);
            logger.debug("BulkIndex response: errors: {}, tooK: {}", queryResponse.getErrors(), queryResponse.getTook());

            if (queryResponse.getErrors()) {
                List<IndexResponse> errors = queryResponse.getItems()
                        .stream()
                        .map(item -> item.getIndex())
                        .filter(item -> item.getError() != null)
                        .collect(Collectors.toList());

                errors.forEach(index -> {
                    logger.error("Failed to index document {} caused by {}", index.getId(), index.getError());
                });
                return errors.size();
            }

            return 0;
        } catch (ResponseException e) {
            logger.error("Elasticsearch Index error for index", e);

            throw new IndexDocumentException(e.getResponse().getStatusLine());
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new IndexDocumentException("Error when executing a document");
        }
    }

    private <T> void addIdToEntity(String id, T source) {
        Method setIdMethod;
        try {
            setIdMethod = source.getClass().getMethod("setId", String.class);
            setIdMethod.invoke(source, id);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new QueryExecutionException("The setter for the id method is not available.", e);
        } catch (IllegalAccessException e) {
            throw new QueryExecutionException("Id argument seems to be wrong", e);
        }
    }
}
