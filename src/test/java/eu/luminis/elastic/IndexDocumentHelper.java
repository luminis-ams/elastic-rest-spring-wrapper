package eu.luminis.elastic;

import eu.luminis.elastic.document.DocumentService;
import eu.luminis.elastic.document.IndexRequest;
import eu.luminis.elastic.document.helpers.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class IndexDocumentHelper {
    @Autowired
    private DocumentService documentService;

    public void indexDocument(String index, String type, String id, String message) {
        indexDocument(index, type, id, message, 2000L);
    }

    public void indexDocument(String index, String type, String id, String message, Long year) {
        indexDocument(index, type, id, message, null, year, null);
    }

    public void indexDocument(String index,
                              String type,
                              String id,
                              String message,
                              List<String> tags,
                              long year,
                              Date created) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessage(message);
        messageEntity.setTags(tags);
        messageEntity.setYear(year);
        messageEntity.setCreated(created);

        IndexRequest indexRequest = new IndexRequest(index, type, id);
        indexRequest.setEntity(messageEntity);

        documentService.index(indexRequest);
    }

}
