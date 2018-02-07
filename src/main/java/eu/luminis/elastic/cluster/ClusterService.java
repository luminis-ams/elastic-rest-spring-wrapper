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
import java.util.Set;

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
     * @param clusterName Name of the cluster to check the health for
     * @return ClusterHealth containing the basic properties of the health of the cluster
     */
    public ClusterHealth checkClusterHealth(String clusterName) {
        try {
            Response response = clusterManagementService.getRestClientForCluster(clusterName).performRequest(GET, "/_cluster/health");

            HttpEntity entity = response.getEntity();

            return jacksonObjectMapper.readValue(entity.getContent(), ClusterHealth.class);

        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new ClusterApiException("Error when checking the health of the cluster");
        }
    }

    /**
     * Returns the set of available clusters by name provided when registering the cluster.
     *
     * @return The names of the available clusters
     */
    public Set<String> getAvailableClusters() {
        return this.clusterManagementService.listAvailableClusters();
    }

    /**
     * Add a new cluster to be managed.
     *
     * @param clusterName the name of the cluster.
     * @param hosts       the hosts within that cluster.
     */
    public void addCluster(String clusterName, List<String> hosts, boolean enableSniffer) {
        clusterManagementService.addCluster(clusterName, hosts, enableSniffer);
    }

    /**
     * Delete a cluster. Also closes any active connections that may exist with this cluster.
     *
     * @param clusterName the name of the cluster.
     */
    public void deleteCluster(String clusterName) {
        clusterManagementService.deleteCluster(clusterName);
    }
}
