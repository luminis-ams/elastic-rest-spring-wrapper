package eu.luminis.elastic.cluster;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Optional<Cluster> getCurrentCluster() {
        return (currentCluster == null) ? Optional.empty() : Optional.of(currentCluster);
    }

    public Cluster setCurrentCluster(String clusterName) {
        Cluster cluster = clusters.get(clusterName);
        if(cluster != null) {
            currentCluster = cluster;
        }
        return currentCluster;
    }

    public Cluster addCluster(String clusterName, List<String> hosts) {
        if(!exists(clusterName)) {
            Cluster cluster = createCluster(hosts);
            clusters.put(clusterName, cluster);
            return cluster;
        } else {
            return null;
        }
    }

    public Cluster deleteCluster(String clusterName) {
        return closeCluster(clusterName);
    }

    @Override
    protected void finalize() {
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
