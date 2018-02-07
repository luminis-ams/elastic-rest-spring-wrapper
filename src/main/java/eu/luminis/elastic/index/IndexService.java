package eu.luminis.elastic.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.luminis.elastic.cluster.ClusterApiException;
import eu.luminis.elastic.cluster.ClusterManagementService;
import eu.luminis.elastic.document.QueryExecutionException;
import eu.luminis.elastic.document.response.Shards;
import eu.luminis.elastic.index.response.RefreshResponse;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
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

    private final ClusterManagementService clusterManagementService;
    private final ObjectMapper jacksonObjectMapper;

    /**
     * Service containing functionality to manage indexes.
     *
     * @param clusterManagementService Used to interact with the clusters
     * @param jacksonObjectMapper      Mapper used to handle responses
     */
    @Autowired
    public IndexService(ClusterManagementService clusterManagementService, ObjectMapper jacksonObjectMapper) {
        this.clusterManagementService = clusterManagementService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    /**
     * Checks if the cluster with the provided cluster name contains an index with the provided index name
     *
     * @param clusterName The name of the cluster to connect to
     * @param indexName   The name of the index to check for existence
     * @return True if the index exists in the cluster with the provided name
     */
    public Boolean indexExist(String clusterName, String indexName) {
        try {
            Response response = clusterManagementService.getRestClientForCluster(clusterName).performRequest(
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

    /**
     * Create a new index with the name {@code indexName} in the cluster with the provided {@code clusterName}. The
     * index settings and mappings can be provided using the {@code requestBody}
     *
     * @param clusterName The name of the cluster to create the index in.
     * @param indexName   The name of the index to create
     * @param requestBody The mappings and settings for the index to be created
     */
    public void createIndex(String clusterName, String indexName, String requestBody) {
        try {
            HttpEntity entity = new StringEntity(requestBody);
            Response response = clusterManagementService.getRestClientForCluster(clusterName).performRequest(
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

    /**
     * Drop the index with the provided {@code indexName} in the cluster with the provided {@code clusterName}
     *
     * @param clusterName The name of the cluster to drop the index from
     * @param indexName   The name of the index to drop
     */
    public void dropIndex(String clusterName, String indexName) {
        try {
            Response response = clusterManagementService.getRestClientForCluster(clusterName).performRequest(
                    "DELETE",
                    indexName);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 499) {
                logger.warn("Problem while deleting an index: {}", response.getStatusLine().getReasonPhrase());
                throw new QueryExecutionException("Could not delete index, status code is " + statusCode);
            }
        } catch (IOException e) {
            logger.warn("Problem deleting index.");
            throw new IndexApiException("Problem deleting index", e);
        }

    }

    /**
     * Refresh the provided indexes {@code names} in the cluster with the provided name {@code clusterName}
     *
     * @param clusterName The name of the cluster to refresh the indexes from
     * @param names The names of the indexes to be refreshed
     */
    public void refreshIndexes(String clusterName, String... names) {
        try {
            String endpoint = "/_refresh";
            if (names.length > 0) {
                endpoint = "/" + String.join(",", names) + endpoint;
            }

            Response response = clusterManagementService.getRestClientForCluster(clusterName).performRequest(
                    "POST",
                    endpoint
            );

            if (response.getStatusLine().getStatusCode() > 399 && logger.isWarnEnabled()) {
                logger.warn("Problem while refreshing indexes: {}", String.join(",", names));
            }

            if (logger.isDebugEnabled()) {
                HttpEntity entity = response.getEntity();

                RefreshResponse refreshResponse = jacksonObjectMapper.readValue(entity.getContent(), RefreshResponse.class);
                Shards shards = refreshResponse.getShards();
                logger.debug("Shards refreshed: total {}, successfull {}, failed {}", shards.getTotal(), shards.getSuccessful(), shards.getFailed());
            }
        } catch (IOException e) {
            logger.warn("Problem while executing refresh request.", e);
            throw new ClusterApiException("Error when refreshing indexes." + e.getMessage());
        }

    }
}
