package eu.luminis.elastic.search;

import eu.luminis.elastic.ElasticTestCase;
import eu.luminis.elastic.IndexDocumentHelper;
import eu.luminis.elastic.RestClientConfig;
import eu.luminis.elastic.document.helpers.MessageEntity;
import eu.luminis.elastic.document.helpers.MessageEntityTypeReference;
import eu.luminis.elastic.index.IndexService;
import eu.luminis.elastic.search.response.Aggregation;
import eu.luminis.elastic.search.response.HitsAggsResponse;
import eu.luminis.elastic.search.response.TermsAggregation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class SearchServiceAggsTest extends ElasticTestCase {
    public static final String TEST_AGGS = "test_aggs";
    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexDocumentHelper indexDocumentHelper;

    @Autowired
    private SearchService searchService;

    @Before
    public void setUp() throws Exception {
        Boolean test_aggs = indexService.indexExist("test_aggs");
        if (test_aggs) {
            indexService.dropIndex(TEST_AGGS);
        }
        String indexProps = "{\n" +
                "  \"settings\": {\n" +
                "    \"number_of_replicas\": 0,\n" +
                "    \"number_of_shards\": 1\n" +
                "  },\n" +
                "  \"mappings\": {\n" +
                "    \"test_agg\": {\n" +
                "      \"properties\": {\n" +
                "        \"message\": {\n" +
                "          \"type\": \"text\"\n" +
                "        },\n" +
                "        \"tags\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        indexService.createIndex(TEST_AGGS, indexProps);
        test_aggs = indexService.indexExist("test_aggs");
        assertTrue("The index should now exist", test_aggs);

        indexDocumentHelper.indexDocument(TEST_AGGS,"test_agg","one","message one", Arrays.asList("news"));
        indexDocumentHelper.indexDocument(TEST_AGGS,"test_agg","two","message two", Arrays.asList("blog"));
        indexDocumentHelper.indexDocument(TEST_AGGS,"test_agg","three","message three", Arrays.asList("news"));
        indexDocumentHelper.indexDocument(TEST_AGGS,"test_agg","four","message four", Arrays.asList("twitter"));
        indexService.refreshIndexes(TEST_AGGS);
    }

    @Test
    public void findTerms() {
        SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(TEST_AGGS)
                .setTemplateName("find_message_aggs.twig")
                .setAddId(true)
                .setTypeReference(new MessageEntityTypeReference());

        HitsAggsResponse<MessageEntity> response = searchService.aggsByTemplate(request);

        assertEquals(4, response.getHits().size());
        Aggregation byTags = response.getAggregations().get("byTags");
        assertNotNull(byTags);
        assertTrue(byTags instanceof TermsAggregation);
        TermsAggregation termsbyTags = (TermsAggregation) byTags;
        assertEquals(3, termsbyTags.getBuckets().size());
    }

    @Test
    public void findTermsNoAggs() {
        SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(TEST_AGGS)
                .setTemplateName("find_message.twig")
                .setAddId(true)
                .setTypeReference(new MessageEntityTypeReference());

        HitsAggsResponse<MessageEntity> response = searchService.aggsByTemplate(request);

        assertEquals(4, response.getHits().size());
        assertNull(response.getAggregations());
    }

    @Test
    public void findTerms_noHits() {
        SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(TEST_AGGS)
                .setTemplateName("find_message_aggs_no_hits.twig")
                .setAddId(true)
                .setTypeReference(new MessageEntityTypeReference());

        HitsAggsResponse<MessageEntity> response = searchService.aggsByTemplate(request);

        assertEquals(0, response.getHits().size());
        Aggregation byTags = response.getAggregations().get("byTags");
        assertNotNull(byTags);
        assertTrue(byTags instanceof TermsAggregation);
        TermsAggregation termsbyTags = (TermsAggregation) byTags;
        assertEquals(3, termsbyTags.getBuckets().size());
    }

}
