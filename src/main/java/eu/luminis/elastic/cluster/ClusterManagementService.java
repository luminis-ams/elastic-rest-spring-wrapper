package eu.luminis.elastic.cluster;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PreDestroy;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.sniff.Sniffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.luminis.elastic.LoggingFailureListener;

/**
 * This service facilitates setting up connections to different ES clusters.
 */
@Component
public class ClusterManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterManagementService.class);
    private static final String HEADER_CONTENT_TYPE_KEY = "Content-Type";
    private static final String DEFAULT_HEADER_CONTENT_TYPE = "application/json";

    private LoggingFailureListener loggingFailureListener;
    private Map<String, Cluster> clusters = new HashMap<>();
    private Cluster currentCluster;

    @Value("${enableSniffer:true}")
    private boolean enableSniffer = true;

    @Autowired
    public ClusterManagementService(LoggingFailureListener loggingFailureListener) {
        this.loggingFailureListener = loggingFailureListener;
    }

    /**
     * Gets the current cluster - there can only be one current cluster at a time.
     * @return the current cluster that the service is working with, or an empty Optional if no cluster has been set.
     */
    public Optional<Cluster> getCurrentCluster() {
        return (currentCluster == null) ? Optional.empty() : Optional.of(currentCluster);
    }

    /**
     * Set the current cluster.
     * @param clusterName the name of the cluster. Make sure that the given clusterName has been added before using {@link #addCluster(String, List)}}.
     * @return the corresponding {@link Cluster} object if a cluster exists by that name, else return the current cluster that was already set.
     */
    public Cluster setCurrentCluster(String clusterName) {
        Cluster cluster = clusters.get(clusterName);
        if(cluster != null) {
            currentCluster = cluster;
        }
        return currentCluster;
    }

    /**
     * Add a new cluster to be managed.
     * @param clusterName the name of the cluster.
     * @param hosts the hosts within that cluster.
     * @return the cluster that has just been added, or null if the clusterName was already present.
     */
    public Cluster addCluster(String clusterName, List<String> hosts) {
        if(!exists(clusterName)) {
            Cluster cluster = createCluster(hosts);
            clusters.put(clusterName, cluster);
            return cluster;
        } else {
            return null;
        }
    }

    /**
     * Delete a cluster. Also closes any active connections that may exist with this cluster.
     * @param clusterName the name of the cluster.
     * @return the closed cluster.
     */
    public Cluster deleteCluster(String clusterName) {
        return closeCluster(clusterName);
    }

    @PreDestroy
    protected void tearDown() {
        clusters.keySet().forEach(this::closeCluster);
    }

    private boolean exists(String clusterName) {
        return clusters.get(clusterName) != null;
    }

    private Cluster createCluster(List<String> hosts) {
        HttpHost[] nodes = hosts.stream().map(HttpHost::create).toArray(HttpHost[]::new);
        Header[] defaultHeaders = new Header[] { new BasicHeader(HEADER_CONTENT_TYPE_KEY, DEFAULT_HEADER_CONTENT_TYPE) };

        RestClient client = RestClient.builder(nodes)
                .setDefaultHeaders(defaultHeaders)
                .setFailureListener(loggingFailureListener)
                .build();

        if (enableSniffer) {
            return new Cluster(client, Sniffer.builder(client).build());
        } else {
            return new Cluster(client);
        }
    }

    private Cluster closeCluster(String clusterName) {
        Cluster cluster = clusters.get(clusterName);
        if(cluster != null) {
            try {
                if (cluster.getSniffer() != null) {
                    cluster.getSniffer().close();
                }
                cluster.getClient().close();
            } catch (IOException ex) {
                String error = "Could not close connection to cluster " + clusterName;
                LOGGER.error(error, ex);
                throw new ClusterApiException(error, ex);
            }
            return clusters.remove(clusterName);
        } else {
            return null;
        }
    }
}
