package eu.luminis.elastic.cluster;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.sniff.Sniffer;

/**
 * Model object representing a connection to a cluster
 */
public class Cluster {

    private RestClient client;
    private Sniffer sniffer;

    /**
     * Default constructor for the cluster
     *
     * @param client  rest client that provides access to the cluster
     * @param sniffer sniffer that notices differences in the cluster architecture
     */
    public Cluster(RestClient client, Sniffer sniffer) {
        this.client = client;
        this.sniffer = sniffer;
    }

    /**
     * Constructor for a cluster that does not use a sniffer
     *
     * @param client rest client that provides access to the cluster
     */
    public Cluster(RestClient client) {
        this(client, null);
    }

    /**
     * Obtain the active client for this cluster
     *
     * @return rest client
     */
    public RestClient getClient() {
        return client;
    }

    /**
     * Obtain the sniffer for this cluster
     *
     * @return sniffer
     */
    public Sniffer getSniffer() {
        return sniffer;
    }
}