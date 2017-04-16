package eu.luminis.elastic.search;

import eu.luminis.elastic.ElasticTestCase;
import eu.luminis.elastic.IndexDocumentHelper;
import eu.luminis.elastic.RestClientConfig;
import eu.luminis.elastic.document.DocumentService;
import eu.luminis.elastic.document.IndexRequest;
import eu.luminis.elastic.document.helpers.MessageEntity;
import eu.luminis.elastic.document.helpers.MessageEntityTypeReference;
import eu.luminis.elastic.index.IndexService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class SearchServiceTest extends ElasticTestCase {

    private final static String INDEX = "search_index";
    private final static String TYPE = "search_type";

    @Autowired
    private SearchService searchService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexDocumentHelper indexDocumentHelper;

    @Before
    public void setUp() throws Exception {
        indexDocumentHelper.indexDocument(INDEX,TYPE,"elastic_1", "This is a document about elastic", 2000L);
        indexDocumentHelper.indexDocument(INDEX,TYPE,"elastic_2", "Another document about elastic", 2000L);
        indexService.refreshIndexes(INDEX);
    }

    @Test
    public void queryByTemplate() {

        SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(INDEX)
                .setTemplateName("find_message.twig")
                .setAddId(true)
                .setTypeReference(new MessageEntityTypeReference())
                .addModelParam("message", "elastic");

        List<MessageEntity> entities = searchService.queryByTemplate(request);

        assertEquals(2, entities.size());
        List<String> ids = Arrays.asList(entities.get(0).getId(), entities.get(1).getId());
        assertTrue(ids.contains("elastic_1"));
        assertTrue(ids.contains("elastic_2"));
    }

    @Test
    public void countByIndex() {
        long countByIndex = searchService.countByIndex(INDEX);

        assertEquals(2L, countByIndex);
    }

}