package eu.luminis.elastic.document;

import eu.luminis.elastic.ElasticTestCase;
import eu.luminis.elastic.RestClientConfig;
import eu.luminis.elastic.document.helpers.MessageEntity;
import eu.luminis.elastic.document.helpers.MessageEntityByIdTypeReference;
import eu.luminis.elastic.document.helpers.MessageEntityTypeReference;
import eu.luminis.elastic.index.IndexDocumentException;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class DocumentServiceTest extends ElasticTestCase {

    private static final String INDEX = "inttests";
    private static final String TYPE = "inttest";
    private static final String EXISTING_ID_1 = "1";
    private static final String EXISTING_ID_1_MESSAGE = "This is a message";

    @Autowired
    private DocumentService documentService;

    @Autowired
    private IndexService indexService;

    @Before
    public void setUp() throws Exception {
        indexDocument(EXISTING_ID_1, EXISTING_ID_1_MESSAGE);
        indexDocument("elastic_1", "This is a document about elastic");
        indexDocument("elastic_2", "Another document about elastic");

        indexService.refreshIndexes(INDEX);
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

    @Test
    public void querybyId() throws Exception {
        QueryByIdRequest request = QueryByIdRequest.create()
                .setIndex(INDEX)
                .setType(TYPE)
                .setId(EXISTING_ID_1)
                .setTypeReference(new MessageEntityByIdTypeReference());

        MessageEntity entity = documentService.querybyId(request);

        assertNotNull(entity);
        assertEquals(EXISTING_ID_1_MESSAGE, entity.getMessage());
    }

    @Test
    public void querybyId_addId() throws Exception {
        QueryByIdRequest request = QueryByIdRequest.create()
                .setIndex(INDEX)
                .setType(TYPE)
                .setId(EXISTING_ID_1)
                .setAddId(true)
                .setTypeReference(new MessageEntityByIdTypeReference());

        MessageEntity entity = documentService.querybyId(request);

        assertNotNull(entity);
        assertEquals(EXISTING_ID_1_MESSAGE, entity.getMessage());
        assertEquals(EXISTING_ID_1, entity.getId());
    }

    @Test
    public void querybyId_NonExistingId() throws Exception {
        QueryByIdRequest request = QueryByIdRequest.create()
                .setIndex(INDEX)
                .setType(TYPE)
                .setId("non_existing")
                .setTypeReference(new MessageEntityByIdTypeReference());
        try {
            documentService.querybyId(request);
            fail("A QueryByIdNotFoundException should have been thrown");
        } catch (QueryByIdNotFoundException e) {
            assertEquals(INDEX, e.getIndex());
            assertEquals(TYPE, e.getType());
            assertEquals("non_existing", e.getId());
        }
    }

    @Test(expected = QueryByIdNotFoundException.class)
    public void querybyId_NonExistingIndex() throws Exception {
        QueryByIdRequest request = QueryByIdRequest.create()
                .setIndex("NonExisting")
                .setType(TYPE)
                .setId(EXISTING_ID_1)
                .setTypeReference(new MessageEntityByIdTypeReference());

        documentService.querybyId(request);
    }

    @Test
    public void index() {
        MessageEntity entity = new MessageEntity();
        entity.setMessage("An index with an id");

        IndexRequest indexRequest = IndexRequest.create()
                .setIndex(INDEX)
                .setType(TYPE)
                .setId("index_1")
                .setEntity(entity);

        String id = documentService.index(indexRequest);

        assertEquals("index_1", id);
    }

    @Test
    public void index_noId() {
        MessageEntity entity = new MessageEntity();
        entity.setMessage("An index without an id");

        IndexRequest indexRequest = IndexRequest.create()
                .setIndex(INDEX)
                .setType(TYPE)
                .setEntity(entity);

        String id = documentService.index(indexRequest);

        // This is a default generated id by elasticsearch, therefore this should work
        assertEquals(20, id.length());
    }

    @Test
    public void remove() {
        MessageEntity entity = new MessageEntity();
        entity.setMessage("An index with an id to be deleted");

        IndexRequest indexRequest = IndexRequest.create()
                .setIndex(INDEX)
                .setType(TYPE)
                .setId("delete_id")
                .setEntity(entity);

        String id = documentService.index(indexRequest);
        assertEquals("delete_id", id);

        String remove = documentService.remove(INDEX, TYPE, id);

        assertEquals("OK", remove);
    }

    @Test(expected = IndexDocumentException.class)
    public void remove_nonExisting() {
        documentService.remove(INDEX, TYPE, "non_existing_delete");
    }

}