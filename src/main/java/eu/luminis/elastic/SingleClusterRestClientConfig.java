package eu.luminis.elastic;

import eu.luminis.elastic.cluster.ClusterManagementService;
import eu.luminis.elastic.cluster.ClusterService;
import eu.luminis.elastic.cluster.SingleClusterClusterService;
import eu.luminis.elastic.document.DocumentService;
import eu.luminis.elastic.document.SingleClusterDocumentService;
import eu.luminis.elastic.index.IndexService;
import eu.luminis.elastic.index.SingleClusterIndexService;
import eu.luminis.elastic.search.SearchService;
import eu.luminis.elastic.search.SingleClusterSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * This configuration should be used when working with just a single cluster.
 */
@Configuration
@Import(RestClientConfig.class)
public class SingleClusterRestClientConfig {

    @Bean
    public SingleClusterRestClientFactoryBean singleClusterRestClientFactoryBean(ClusterManagementService clusterManagementService) {
        return new SingleClusterRestClientFactoryBean(clusterManagementService);
    }

    @Bean
    public SingleClusterSearchService singleClusterSearchService(SearchService searchService) {
        return new SingleClusterSearchService(searchService);
    }

    @Bean
    public SingleClusterDocumentService singleClusterDocumentService(DocumentService documentService) {
        return new SingleClusterDocumentService(documentService);
    }

    @Bean
    public SingleClusterIndexService singleClusterIndexService(IndexService indexService) {
        return new SingleClusterIndexService(indexService);
    }

    @Bean
    public SingleClusterClusterService singleClusterClusterService(ClusterService clusterService) {
        return new SingleClusterClusterService(clusterService);
    }
}
