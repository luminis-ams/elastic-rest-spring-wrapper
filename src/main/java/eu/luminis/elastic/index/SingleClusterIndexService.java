package eu.luminis.elastic.index;

import static eu.luminis.elastic.SingleClusterRestClientFactoryBean.DEFAULT_CLUSTER_NAME;

/**
 * Wrapper for the {@code IndexService} without the option to work with multiple clusters. Only the default cluster
 * is available.
 */
public class SingleClusterIndexService {
    private IndexService indexService;


    /**
     * Default constructor
     *
     * @param indexService wrapped index service
     */
    public SingleClusterIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * Checks if the cluster with the provided cluster name contains an index with the provided index name
     *
     * @param indexName The name of the index to check for existence
     * @return True if the index exists in the cluster with the provided name
     */
    public Boolean indexExist(String indexName) {
        return indexService.indexExist(DEFAULT_CLUSTER_NAME, indexName);
    }

    /**
     * Create a new index with the name {@code indexName} in the cluster with the provided {@code clusterName}. The
     * index settings and mappings can be provided using the {@code requestBody}
     *
     * @param indexName   The name of the index to create
     * @param requestBody The mappings and settings for the index to be created
     */
    public void createIndex(String indexName, String requestBody) {
        indexService.createIndex(DEFAULT_CLUSTER_NAME, indexName, requestBody);
    }

    /**
     * Drop the index with the provided {@code indexName} in the cluster with the provided {@code clusterName}
     *
     * @param indexName The name of the index to drop
     */
    public void dropIndex(String indexName) {
        indexService.dropIndex(DEFAULT_CLUSTER_NAME, indexName);
    }

    /**
     * Refresh the provided indexes {@code names} in the cluster with the provided name {@code clusterName}
     *
     * @param names The names of the indexes to be refreshed
     */
    public void refreshIndexes(String... names) {
        indexService.refreshIndexes(DEFAULT_CLUSTER_NAME, names);
    }
}
