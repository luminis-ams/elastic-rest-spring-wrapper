package eu.luminis.elastic;

import java.util.Arrays;

import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;

import eu.luminis.elastic.cluster.ClusterManagementService;

/**
 * Factory bean for creating the RestClient instance(s)
 */
@Component
public class RestClientFactoryBean extends AbstractFactoryBean<RestClient> {

    private static final String DEFAULT_CLUSTER_NAME = "default-cluster";
    private ClusterManagementService factory;

    private String[] hostnames;

    @Autowired
    public RestClientFactoryBean(ClusterManagementService factory) {
        this.factory = factory;
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    protected RestClient createInstance() {
        return factory.addCluster(DEFAULT_CLUSTER_NAME, Arrays.asList(hostnames)).getClient();
    }

    @Override
    protected void destroyInstance(RestClient instance) {
        factory.deleteCluster(DEFAULT_CLUSTER_NAME);
    }

    @Value("${eu.luminis.elastic.hostnames:#{\"localhost:9200\"}}")
    public void setHostnames(String[] hostnames) {
        this.hostnames = hostnames;
    }
}
