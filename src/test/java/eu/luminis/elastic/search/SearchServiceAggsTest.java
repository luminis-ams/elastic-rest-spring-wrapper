package eu.luminis.elastic.search;

import eu.luminis.elastic.ElasticTestCase;
import eu.luminis.elastic.IndexDocumentHelper;
import eu.luminis.elastic.RestClientConfig;
import eu.luminis.elastic.document.helpers.MessageEntity;
import eu.luminis.elastic.document.helpers.MessageEntityTypeReference;
import eu.luminis.elastic.index.IndexService;
import eu.luminis.elastic.search.response.Aggregation;
import eu.luminis.elastic.search.response.DateHistogramAggregation;
import eu.luminis.elastic.search.response.DateHistogramBucket;
import eu.luminis.elastic.search.response.HistogramAggregation;
import eu.luminis.elastic.search.response.HistogramBucket;
import eu.luminis.elastic.search.response.HitsAggsResponse;
import eu.luminis.elastic.search.response.TermsAggregation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class SearchServiceAggsTest extends ElasticTestCase {
    public static final String TEST_AGGS = "test_aggs";
    public static final int NUM_DAYS_TO_SUBTRACT_1 = 1;
    public static final int NUM_DAYS_TO_SUBTRACT_2 = 2;
    public static final int NUM_DAYS_TO_SUBTRACT_5 = 5;
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Amsterdam");
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
                "        },\n" +
                "        \"year\": {\n" +
                "          \"type\": \"long\"\n" +
                "        },\n" +
                "        \"created\": {\n" +
                "          \"type\": \"date\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        indexService.createIndex(TEST_AGGS, indexProps);
        test_aggs = indexService.indexExist("test_aggs");
        assertTrue("The index should now exist", test_aggs);

        indexDocumentHelper.indexDocument(TEST_AGGS, "test_agg", "one", "message one", Arrays.asList("news"), 1970L, createDate(NUM_DAYS_TO_SUBTRACT_1));
        indexDocumentHelper.indexDocument(TEST_AGGS, "test_agg", "two", "message two", Arrays.asList("blog"), 1980L, createDate(NUM_DAYS_TO_SUBTRACT_1));
        indexDocumentHelper.indexDocument(TEST_AGGS, "test_agg", "three", "message three", Arrays.asList("news"), 1985L, createDate(NUM_DAYS_TO_SUBTRACT_2));
        indexDocumentHelper.indexDocument(TEST_AGGS, "test_agg", "four", "message four", Arrays.asList("twitter"), 2000, createDate(NUM_DAYS_TO_SUBTRACT_5));
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

    @Test
    public void findHistogramByYear() {
        SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(TEST_AGGS)
                .setTemplateName("find_message_range_aggs_no_hits.twig")
                .setAddId(true)
                .setTypeReference(new MessageEntityTypeReference());

        HitsAggsResponse<MessageEntity> response = searchService.aggsByTemplate(request);

        assertEquals(0, response.getHits().size());
        Aggregation byHistogram = response.getAggregations().get("byHistogram");
        assertNotNull(byHistogram);
        assertTrue(byHistogram instanceof HistogramAggregation);
        HistogramAggregation histoAggs = (HistogramAggregation) byHistogram;
        assertEquals(4, histoAggs.getBuckets().size());

        checkHistoBucket(histoAggs.getBuckets().get(0), 1, "1970.0");
        checkHistoBucket(histoAggs.getBuckets().get(1), 2, "1980.0");
        checkHistoBucket(histoAggs.getBuckets().get(2), 0, "1990.0");
        checkHistoBucket(histoAggs.getBuckets().get(3), 1, "2000.0");
    }

    @Test
    public void findDateHistogramByDay() {
        SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(TEST_AGGS)
                .setTemplateName("find_message_date_histo_no_hits.twig")
                .setAddId(true)
                .setTypeReference(new MessageEntityTypeReference());

        HitsAggsResponse<MessageEntity> response = searchService.aggsByTemplate(request);

        assertEquals(0, response.getHits().size());
        assertEquals(4, response.getTotalHits());

        Aggregation byDateHistogram = response.getAggregations().get("byDateHistogram");
        assertNotNull(byDateHistogram);
        assertTrue(byDateHistogram instanceof DateHistogramAggregation);
        DateHistogramAggregation histoAggs = (DateHistogramAggregation) byDateHistogram;
        assertEquals(5, histoAggs.getBuckets().size());

        checkDateHistoBucket(histoAggs.getBuckets().get(0),
                1, expectedMilis(NUM_DAYS_TO_SUBTRACT_5), expectKeyAsString(NUM_DAYS_TO_SUBTRACT_5));
        checkDateHistoBucket(histoAggs.getBuckets().get(1),
                0, expectedMilis(4), expectKeyAsString(4));
        checkDateHistoBucket(histoAggs.getBuckets().get(2),
                0, expectedMilis(3), expectKeyAsString(3));
        checkDateHistoBucket(histoAggs.getBuckets().get(3),
                1, expectedMilis(NUM_DAYS_TO_SUBTRACT_2), expectKeyAsString(NUM_DAYS_TO_SUBTRACT_2));
        checkDateHistoBucket(histoAggs.getBuckets().get(4),
                2, expectedMilis(NUM_DAYS_TO_SUBTRACT_1), expectKeyAsString(NUM_DAYS_TO_SUBTRACT_1));
    }

    private void checkHistoBucket(HistogramBucket bucket, long expectedCount, String expectedKey) {
        assertEquals(expectedCount, bucket.getDocCount().longValue());
        assertEquals(expectedKey, bucket.getKey());
    }

    private void checkDateHistoBucket(DateHistogramBucket bucket,
                                      long expectedCount,
                                      long expectedKey,
                                      String expectedKeyAsString) {
        assertEquals(expectedCount, bucket.getDocCount().longValue());
        assertEquals(expectedKey, bucket.getKey().longValue());
        assertEquals(expectedKeyAsString, bucket.getKeyAsString());
    }

    private Date createDate(int numDaysToSubtract) {
        LocalDateTime date = LocalDateTime.now();

        LocalDateTime newDate = date.minusDays(numDaysToSubtract);

        return Date.from(newDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    private long expectedMilis(int numDaysToSubtract) {
        LocalDate date = LocalDate.now();

        LocalDateTime newDate = LocalDateTime.from(date.minusDays(numDaysToSubtract).atStartOfDay(ZONE_ID));

        return Date.from(newDate.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    private String expectKeyAsString(int numDaysToSubtract) {
        LocalDate date = LocalDate.now();

        LocalDate newDate = date.minusDays(numDaysToSubtract);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        return newDate.format(formatter);
    }
}
