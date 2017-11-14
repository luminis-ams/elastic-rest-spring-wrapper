package eu.luminis.elastic.document;

import eu.luminis.elastic.IndexDocumentHelper;
import eu.luminis.elastic.RestClientConfig;
import eu.luminis.elastic.document.helpers.MessageEntity;
import eu.luminis.elastic.document.helpers.MessageEntityByIdTypeReference;
import eu.luminis.elastic.index.IndexService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class DocumentServiceIT {

    private static final String INDEX = "inttests";
    private static final String TYPE = "inttest";
    private static final String EXISTING_ID_1 = "1";
    private static final String EXISTING_ID_1_MESSAGE = "This is a message";

    @Autowired
    private DocumentService documentService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexDocumentHelper indexDocumentHelper;

    @Before
    public void setUp() throws Exception {
        indexDocumentHelper.indexDocument(INDEX, TYPE, EXISTING_ID_1, EXISTING_ID_1_MESSAGE);
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_1", "This is a document about elastic");
        indexDocumentHelper.indexDocument(INDEX, TYPE, "elastic_2", "Another document about elastic");

        indexService.refreshIndexes(INDEX);
    }

    @Test
    public void querybyId() throws Exception {
        QueryByIdRequest request = new QueryByIdRequest(INDEX, TYPE, EXISTING_ID_1);
        request.setTypeReference(new MessageEntityByIdTypeReference());
        request.setRefresh(Refresh.NOW);
        MessageEntity entity = documentService.queryById(request);

        assertNotNull(entity);
        assertEquals(EXISTING_ID_1_MESSAGE, entity.getMessage());
    }

    @Test
    public void querybyId_addId() throws Exception {
        QueryByIdRequest request = new QueryByIdRequest(INDEX, TYPE, EXISTING_ID_1);
        request.setAddId(true);
        request.setTypeReference(new MessageEntityByIdTypeReference());

        MessageEntity entity = documentService.queryById(request);

        assertNotNull(entity);
        assertEquals(EXISTING_ID_1_MESSAGE, entity.getMessage());
        assertEquals(EXISTING_ID_1, entity.getId());
    }

    @Test
    public void querybyId_NonExistingId() throws Exception {
        QueryByIdRequest request = new QueryByIdRequest(INDEX, TYPE, "non_existing");
        request.setTypeReference(new MessageEntityByIdTypeReference());
        try {
            documentService.queryById(request);
            fail("A QueryByIdNotFoundException should have been thrown");
        } catch (QueryByIdNotFoundException e) {
            assertEquals(INDEX, e.getIndex());
            assertEquals(TYPE, e.getType());
            assertEquals("non_existing", e.getId());
        }
    }

    @Test
    public void querybyId_NoTypeReference() throws Exception {
        QueryByIdRequest request = new QueryByIdRequest(INDEX, TYPE, "non_existing");
        try {
            documentService.queryById(request);
            fail("A QueryExecutionException should have been thrown");
        } catch (QueryExecutionException e) {
            assertEquals("The TypeReference in the request cannot be null", e.getMessage());
        }
    }

    @Test(expected = QueryByIdNotFoundException.class)
    public void querybyId_NonExistingIndex() throws Exception {
        QueryByIdRequest request = new QueryByIdRequest("NonExisting", TYPE, EXISTING_ID_1);
        request.setTypeReference(new MessageEntityByIdTypeReference());

        documentService.queryById(request);
    }

    @Test
    public void index() {
        MessageEntity entity = new MessageEntity();
        entity.setMessage("An index with an id");

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, "index_1");
        indexRequest.setEntity(entity);

        String id = documentService.index(indexRequest);

        assertEquals("index_1", id);
    }

    @Test
    public void index_noId() {
        MessageEntity entity = new MessageEntity();
        entity.setMessage("An index without an id");

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE);
        indexRequest.setEntity(entity);

        String id = documentService.index(indexRequest);

        // This is a default generated id by elasticsearch, therefore this should work
        assertEquals(20, id.length());
    }

    @Test
    public void remove() {
        MessageEntity entity = new MessageEntity();
        entity.setMessage("An index with an id to be deleted");

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, "delete_id");
        indexRequest.setEntity(entity);

        String id = documentService.index(indexRequest);
        assertEquals("delete_id", id);

        String remove = documentService.remove(new DeleteRequest(INDEX, TYPE, id));

        assertEquals("OK", remove);
    }

    @Test(expected = IndexDocumentException.class)
    public void remove_nonExisting() {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, "non_existing_delete");
        deleteRequest.setMustExist(true);
        documentService.remove(deleteRequest);
    }

    @Test
    public void remove_nonExisting_mustExistFalse() {
        documentService.remove(new DeleteRequest(INDEX, TYPE, "non_existing_delete"));
    }

    @Test
    public void updateDocument() {
        MessageEntity entity = new MessageEntity();
        entity.setId("test_update");

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, "test_update");
        indexRequest.setEntity(entity);
        indexRequest.setRefresh(Refresh.NOW);
        documentService.index(indexRequest);

        entity.setMessage("Updated message");

        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, "test_update");
        updateRequest.setEntity(entity);
        documentService.update(updateRequest);

        QueryByIdRequest test_update = new QueryByIdRequest(INDEX, TYPE, "test_update");
        test_update.setTypeReference(new MessageEntityByIdTypeReference());
        MessageEntity updatedMessage = documentService.queryById(test_update);

        assertEquals("Updated message", updatedMessage.getMessage());
    }

    @Test
    public void create_existing_id() {
        MessageEntity entity = new MessageEntity();
        entity.setMessage("An create index with an existing id");

        IndexRequest indexRequest = new IndexRequest();
        indexRequest.setIndex(INDEX).setType(TYPE).setId("create_1");
        indexRequest.setEntity(entity);

        documentService.index(indexRequest);

        try {
            documentService.create(indexRequest);
            fail("An IndexDocumentException should have been thrown");
        } catch (IndexDocumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void create_new_id() {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, "create_2");
            documentService.remove(deleteRequest);
        } catch (IndexDocumentException e) {
            // don't care, if not running with clean install or clean index need to delete, in the other cases not.
        }

        MessageEntity entity = new MessageEntity();
        entity.setMessage("An create index with an new id");

        IndexRequest indexRequest = new IndexRequest();
        indexRequest.setIndex(INDEX).setType(TYPE).setId("create_2");
        indexRequest.setEntity(entity);

        String result = documentService.create(indexRequest);
        assertNotNull(result);
    }

    @Test
    public void create_no_id() {
        MessageEntity entity = new MessageEntity();
        entity.setMessage("An create index without an id");

        IndexRequest indexRequest = new IndexRequest();
        indexRequest.setIndex(INDEX).setType(TYPE);
        indexRequest.setEntity(entity);

        try {
            documentService.create(indexRequest);
            fail("A QueryExecutionException should have been thrown as there is no id");
        } catch (QueryExecutionException e) {
            assertEquals("Executing create request without an id", e.getMessage());
        }
    }

    @Test
    public void exists() {
        ExistsRequest existsRequest = new ExistsRequest(INDEX, TYPE, "elastic_1");
        Boolean exists = documentService.exists(existsRequest);

        assertTrue(exists);
    }

    @Test
    public void existsNotExist() {
        ExistsRequest existsRequest = new ExistsRequest(INDEX, TYPE, "elastic_not_exist");
        Boolean exists = documentService.exists(existsRequest);

        assertFalse(exists);
    }

    @Test
    public void existsNoId() {
        ExistsRequest existsRequest = new ExistsRequest(INDEX, TYPE, null);
        Boolean exists = documentService.exists(existsRequest);

        assertFalse(exists);
    }
}