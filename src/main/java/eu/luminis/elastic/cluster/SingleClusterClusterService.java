package eu.luminis.elastic.cluster;

import eu.luminis.elastic.cluster.response.ClusterHealth;

import static eu.luminis.elastic.SingleClusterRestClientFactoryBean.DEFAULT_CLUSTER_NAME;

/**
 * Wrapper for the {@code ClusterService} without the option to work with multiple clusters. Only the default cluster
 * is available.
 */
public class SingleClusterClusterService {
    private ClusterService clusterService;

    /**
     * Default constructor
     * @param clusterService ClusterService that is wrapped
     */
    public SingleClusterClusterService(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    /**
     * Returns the current health of the cluster as a {@link ClusterHealth} object.
     *
     * @return ClusterHealth containing the basic properties of the health of the cluster
     */
    public ClusterHealth checkClusterHealth() {
        return this.clusterService.checkClusterHealth(DEFAULT_CLUSTER_NAME);
    }
}
