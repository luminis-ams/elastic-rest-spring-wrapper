package eu.luminis.elastic.search;

import eu.luminis.elastic.search.response.HitsAggsResponse;
import eu.luminis.elastic.search.response.HitsResponse;

import static eu.luminis.elastic.SingleClusterRestClientFactoryBean.DEFAULT_CLUSTER_NAME;

public class SingleClusterSearchService {
    private SearchService searchService;

    public SingleClusterSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public <T> HitsResponse<T> queryByTemplate(SearchByTemplateRequest request) {
        return this.searchService.queryByTemplate(DEFAULT_CLUSTER_NAME,request);
    }

    public <T> HitsAggsResponse<T> aggsByTemplate(SearchByTemplateRequest request) {
        return this.searchService.aggsByTemplate(DEFAULT_CLUSTER_NAME, request);
    }

    public Long countByIndex(String indexName) {
        return this.searchService.countByIndex(DEFAULT_CLUSTER_NAME, indexName);
    }
}
