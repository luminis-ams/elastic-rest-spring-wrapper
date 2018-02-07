package eu.luminis.elastic;

import eu.luminis.elastic.cluster.ClusterManagementService;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.Arrays;

/**
 * Factory bean for creating the RestClient instance when working with a single cluster.
 */
public class SingleClusterRestClientFactoryBean extends AbstractFactoryBean<RestClient> {

    public static final String DEFAULT_CLUSTER_NAME = "default-cluster";
    private final ClusterManagementService clusterManagementService;

    private String[] hostNames;
    private boolean enableSniffer;

    public SingleClusterRestClientFactoryBean(ClusterManagementService clusterManagementService) {
        this.clusterManagementService = clusterManagementService;
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    protected RestClient createInstance() {
        return clusterManagementService
                .addCluster(DEFAULT_CLUSTER_NAME, Arrays.asList(hostNames), enableSniffer)
                .getClient();
    }

    @Override
    protected void destroyInstance(RestClient instance) {
        clusterManagementService.deleteCluster(DEFAULT_CLUSTER_NAME);
    }

    @Value("${eu.luminis.elastic.hostnames:#{\"localhost:9200\"}}")
    public void setHostNames(String[] hostNames) {
        this.hostNames = hostNames;
    }

    @Value("${enableSniffer:true}")
    public void setEnableSniffer(boolean enableSniffer) {
        this.enableSniffer = enableSniffer;
    }
}
