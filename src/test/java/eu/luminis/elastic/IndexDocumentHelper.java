package eu.luminis.elastic;

import eu.luminis.elastic.document.DocumentService;
import eu.luminis.elastic.document.IndexRequest;
import eu.luminis.elastic.document.helpers.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class IndexDocumentHelper {
    @Autowired
    DocumentService documentService;

    public void indexDocument(String index, String type, String id, String message) {
        indexDocument(index, type, id, message, null);
    }

    public void indexDocument(String index, String type, String id, String message, List<String> tags) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessage(message);
        messageEntity.setTags(tags);

        IndexRequest indexRequest = new IndexRequest(index, type, id);
        indexRequest.setEntity(messageEntity);

        documentService.index(indexRequest);
    }

}
