package eu.luminis.elastic.index;

import eu.luminis.elastic.document.QueryExecutionException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

/**
 * Exposes index api related services.
 */
@Service
public class IndexService {
    private static final Logger logger = LoggerFactory.getLogger(IndexService.class);

    private final RestClient client;

    @Autowired
    public IndexService(RestClient client) {
        this.client = client;
    }

    public Boolean indexExist(String indexName) {
        try {
            Response response = client.performRequest(
                    "HEAD",
                    indexName
            );

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                return true;
            } else if (statusCode == 404) {
                return false;
            } else {
                logger.warn("Problem while checking index existence: {}", response.getStatusLine().getReasonPhrase());
                throw new QueryExecutionException("Could not check index existence, status code is " + statusCode);
            }
        } catch (IOException e) {
            logger.warn("Problem while verifying if index exists.", e);
            throw new IndexApiException("Error when checking for existing index.");
        }
    }

    public void createIndex(String indexName, String requestBody) {
        try {
            HttpEntity entity = new StringEntity(requestBody);
            Response response = client.performRequest(
                    "PUT",
                    indexName,
                    new Hashtable<>(),
                    entity);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                logger.warn("Problem while creating an index: {}", response.getStatusLine().getReasonPhrase());
                throw new QueryExecutionException("Could not create index, status code is " + statusCode);
            }

        } catch (UnsupportedEncodingException e) {
            logger.warn("Problem converting the request body into an http entity");
            throw new IndexApiException("Problem converting the request body into an http entity", e);
        } catch (IOException e) {
            logger.warn("Problem creating new index.");
            throw new IndexApiException("Problem creating new index", e);
        }
    }
}
