package eu.luminis.elastic.search;

import eu.luminis.elastic.IndexDocumentHelper;
import eu.luminis.elastic.SingleClusterRestClientConfig;
import eu.luminis.elastic.TestConfig;
import eu.luminis.elastic.document.helpers.MessageEntity;
import eu.luminis.elastic.document.helpers.MessageEntityTypeReference;
import eu.luminis.elastic.index.IndexService;
import eu.luminis.elastic.search.response.HitsResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static eu.luminis.elastic.SingleClusterRestClientFactoryBean.DEFAULT_CLUSTER_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SingleClusterRestClientConfig.class, TestConfig.class})
public class SearchServiceIT {

    private final static String INDEX = "search_index";
    private final static String TYPE = "search_type";

    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexDocumentHelper indexDocumentHelper;

    @Autowired
    private SingleClusterSearchService searchService;

    @Before
    public void setUp() throws Exception {
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_1", "This is a document about elastic", 2001L);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_3", "Second document about elastic", 2003L);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_4", "Third document about elastic", 2004L);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_9", "Fourth document about elasticsearch", 2009L);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_7", "Fifth document about elastic", 2007L);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_2", "Sixth document about elastic", 2002L);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_6", "Seventh document about elasticsearch", 2006L);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_8", "Eighth document about elastic", 2008L);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_10", "Ninth document about elasticsearch", 2010L);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_5", "Tenth document about elastic", 2005L);
        indexService.refreshIndexes(DEFAULT_CLUSTER_NAME, INDEX);
    }

    @Test
    public void testQueryByTemplate() {

        SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(INDEX)
                .setTemplateName("find_message.twig")
                .setAddId(true)
                .setTypeReference(new MessageEntityTypeReference())
                .addModelParam("message", "elastic");

        HitsResponse<MessageEntity> hitsResponse = searchService.queryByTemplate(request);
        List<MessageEntity> entities = hitsResponse.getHits();

        assertEquals(7, hitsResponse.getTotalHits());
        assertEquals(7, entities.size());
        List<String> ids = new ArrayList<>();
        entities.forEach(messageEntity -> ids.add(messageEntity.getId()));
        assertTrue(ids.contains("elastic_1"));
        assertTrue(ids.contains("elastic_3"));
        assertTrue(ids.contains("elastic_4"));
        assertTrue(ids.contains("elastic_7"));
        assertTrue(ids.contains("elastic_2"));
        assertTrue(ids.contains("elastic_8"));
        assertTrue(ids.contains("elastic_5"));

        assertFalse(ids.contains("elastic_9"));
        assertFalse(ids.contains("elastic_6"));
        assertFalse(ids.contains("elastic_10"));
    }

    @Test
    public void testSortQueryByTemplate() {
        SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(INDEX)
                .setTemplateName("find_message_sort.twig")
                .setAddId(true)
                .setTypeReference(new MessageEntityTypeReference())
                .addModelParam("sort", "asc");

        HitsResponse<MessageEntity> hitsResponse = searchService.queryByTemplate(request);
        List<MessageEntity> entities = hitsResponse.getHits();

        assertEquals(10, hitsResponse.getTotalHits());
        assertEquals(10, entities.size());
        assertEquals("elastic_1", entities.get(0).getId());
        assertEquals("elastic_2", entities.get(1).getId());
        assertEquals("elastic_3", entities.get(2).getId());
        assertEquals("elastic_4", entities.get(3).getId());
        assertEquals("elastic_5", entities.get(4).getId());
        assertEquals("elastic_6", entities.get(5).getId());
        assertEquals("elastic_7", entities.get(6).getId());
        assertEquals("elastic_8", entities.get(7).getId());
        assertEquals("elastic_9", entities.get(8).getId());
        assertEquals("elastic_10", entities.get(9).getId());
    }

    @Test
    public void testCountByIndex() {
        long countByIndex = searchService.countByIndex(INDEX);

        assertEquals(10L, countByIndex);
    }

}