package eu.luminis.elastic.cluster;

import eu.luminis.elastic.LoggingFailureListener;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.sniff.Sniffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This service facilitates setting up connections to different ES clusters.
 */
@Component
public class ClusterManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterManagementService.class);
    private static final String HEADER_CONTENT_TYPE_KEY = "Content-Type";
    private static final String DEFAULT_HEADER_CONTENT_TYPE = "application/json";

    private Map<String, Cluster> clusters = new HashMap<>();

    /**
     * Return the cluster with the provided name. If the name cannot be found a ClusterApiException is thrown
     *
     * @param clusterName Name of the cluster to return
     * @return The found cluster
     */
    public Cluster getCluster(String clusterName) {
        Cluster cluster = clusters.get(clusterName);

        if (cluster == null) {
            LOGGER.warn("Requested cluster with name {} is unknown.", clusterName);
            throw new ClusterApiException("The provided cluster name does not result in a valid cluster");
        }

        return cluster;
    }

    /**
     * Return the client that maintains the actual connection to the cluster with the provided name
     *
     * @param clusterName The name of the cluster to find the client for.
     * @return The rest client belonging to the cluster with the provided name
     */
    public RestClient getRestClientForCluster(String clusterName) {
        return this.getCluster(clusterName).getClient();
    }

    /**
     * Add a new cluster to be managed.
     *
     * @param clusterName the name of the cluster.
     * @param hosts       the hosts within that cluster.
     * @return the cluster that has just been added, or null if the clusterName was already present.
     */
    public Cluster addCluster(String clusterName, List<String> hosts, boolean enableSniffer) {
        if (!exists(clusterName)) {
            Cluster cluster = createCluster(hosts, enableSniffer);
            clusters.put(clusterName, cluster);
            return cluster;
        } else {
            return null;
        }
    }

    /**
     * Delete a cluster. Also closes any active connections that may exist with this cluster.
     *
     * @param clusterName the name of the cluster.
     * @return the closed cluster.
     */
    public Cluster deleteCluster(String clusterName) {
        Cluster cluster = closeCluster(clusterName);
        clusters.remove(clusterName);
        return cluster;
    }

    /**
     * Returns the names of all registered clusters.
     *
     * @return The names of the registered clusters
     */
    public Set<String> listAvailableClusters() {
        return clusters.keySet();
    }

    @PreDestroy
    protected void tearDown() {
        clusters.keySet().forEach(this::closeCluster);
        clusters.clear();
    }

    private boolean exists(String clusterName) {
        return clusters.get(clusterName) != null;
    }

    private Cluster createCluster(List<String> hosts, boolean enableSniffer) {
        HttpHost[] nodes = hosts.stream().map(HttpHost::create).toArray(HttpHost[]::new);
        Header[] defaultHeaders = new Header[]{new BasicHeader(HEADER_CONTENT_TYPE_KEY, DEFAULT_HEADER_CONTENT_TYPE)};

        RestClient client = RestClient.builder(nodes)
                .setDefaultHeaders(defaultHeaders)
                .setFailureListener(new LoggingFailureListener())
                .build();

        if (enableSniffer) {
            return new Cluster(client, Sniffer.builder(client).build());
        } else {
            return new Cluster(client);
        }
    }

    private Cluster closeCluster(String clusterName) {
        Cluster cluster = clusters.get(clusterName);
        if (cluster != null) {
            try {
                if (cluster.getSniffer() != null) {
                    cluster.getSniffer().close();
                }
                cluster.getClient().close();
            } catch (IOException ex) {
                String error = String.format("Could not close connection to cluster: %s", clusterName);
                LOGGER.error(error, ex);
                throw new ClusterApiException(error, ex);
            }
            return cluster;
        } else {
            return null;
        }
    }
}
