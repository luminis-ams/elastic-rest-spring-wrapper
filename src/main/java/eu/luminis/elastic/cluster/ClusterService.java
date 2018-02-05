package eu.luminis.elastic.cluster;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.luminis.elastic.cluster.response.ClusterHealth;

import org.apache.http.HttpEntity;
import org.elasticsearch.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static eu.luminis.elastic.RequestMethod.GET;

/**
 * Service that provides access to the eu.luminis.elastic cluster services.
 */
@Service
public class ClusterService {
    private static final Logger logger = LoggerFactory.getLogger(ClusterService.class);

    private final ClusterManagementService clusterManagementService;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public ClusterService(ClusterManagementService clusterManagementService, ObjectMapper jacksonObjectMapper) {
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.clusterManagementService = clusterManagementService;
    }

    /**
     * Returns the current health of the cluster as a {@link ClusterHealth} object.
     *
     * @return ClusterHealth containing the basic properties of the health of the cluster
     */
    public ClusterHealth checkClusterHealth() {
        try {
            Response response = clusterManagementService.getCurrentClient().performRequest(GET, "/_cluster/health");

            HttpEntity entity = response.getEntity();

            return jacksonObjectMapper.readValue(entity.getContent(), ClusterHealth.class);

        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new ClusterApiException("Error when checking the health of the cluster");
        }
    }

    /**
     * Get the current cluster that is active at the moment
     * @return the current cluster
     */
    public Cluster getCurrentCluster() {
        return clusterManagementService.getCurrentCluster();
    }

    /**
     * Set the current cluster.
     * @param clusterName the name of the cluster. Make sure that the given clusterName has been added before using {@link #addCluster(String, List)}}.
     * @return the corresponding {@link Cluster} object if a cluster exists by that name, else return the current cluster that was already set.
     */
    public Cluster setCurrentCluster(String clusterName) {
        return clusterManagementService.setCurrentCluster(clusterName);
    }

    /**
     * Add a new cluster to be managed.
     * @param clusterName the name of the cluster.
     * @param hosts the hosts within that cluster.
     * @return the cluster that has just been added, or null if the clusterName was already present.
     */
    public Cluster addCluster(String clusterName, List<String> hosts) {
        return clusterManagementService.addCluster(clusterName, hosts);
    }

    /**
     * Delete a cluster. Also closes any active connections that may exist with this cluster.
     * @param clusterName the name of the cluster.
     * @return the closed cluster.
     */
    public Cluster deleteCluster(String clusterName) {
        return clusterManagementService.deleteCluster(clusterName);
    }
}
