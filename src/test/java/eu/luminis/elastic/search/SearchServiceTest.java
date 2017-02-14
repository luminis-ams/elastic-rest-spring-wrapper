package eu.luminis.elastic.search;

import eu.luminis.elastic.ElasticTestCase;
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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class SearchServiceTest  extends ElasticTestCase {

    private final static String INDEX = "search_index";
    private final static String TYPE = "search_type";

    @Autowired
    private SearchService searchService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private IndexService indexService;

    @Before
    public void setUp() throws Exception {
        indexDocument("elastic_1", "This is a document about elastic");
        indexDocument("elastic_2", "Another document about elastic");
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

    private void indexDocument(String id, String message) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessage(message);

        IndexRequest indexRequest = IndexRequest.create()
                .setIndex(INDEX)
                .setType(TYPE)
                .setId(id)
                .setEntity(messageEntity);

        documentService.index(indexRequest);
    }

}