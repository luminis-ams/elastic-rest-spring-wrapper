package eu.luminis.elastic.cluster;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.sniff.Sniffer;

public class Cluster {

    private RestClient client;
    private Sniffer sniffer;

    public Cluster(RestClient client, Sniffer sniffer) {
        this.client = client;
        this.sniffer = sniffer;
    }

    public Cluster(RestClient client) {
        this.client = client;
    }

    public RestClient getClient() {
        return client;
    }

    public Sniffer getSniffer() {
        return sniffer;
    }
}