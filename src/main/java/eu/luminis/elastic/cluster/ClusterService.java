package eu.luminis.elastic.cluster;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.luminis.elastic.cluster.response.ClusterHealth;
import org.apache.http.HttpEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service that provides access to the eu.luminis.elastic cluster services.
 */
@Service
public class ClusterService {
    private static final Logger logger = LoggerFactory.getLogger(ClusterService.class);

    private final RestClient client;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public ClusterService(RestClient client, ObjectMapper jacksonObjectMapper) {
        this.client = client;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    /**
     * Returns the current health of the cluster as a {@link ClusterHealth} object.
     *
     * @return ClusterHealth containing the basic properties of the health of the cluster
     */
    public ClusterHealth checkClusterHealth() {
        try {
            Response response = client.performRequest(
                    "GET",
                    "/_cluster/health"
            );
            HttpEntity entity = response.getEntity();

            return jacksonObjectMapper.readValue(entity.getContent(), ClusterHealth.class);

        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new ClusterApiException("Error when checking the health of the cluster");
        }
    }

}
