package eu.luminis.elastic;

import eu.luminis.elastic.document.DocumentService;
import eu.luminis.elastic.document.IndexRequest;
import eu.luminis.elastic.document.helpers.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class IndexDocumentHelper {
    @Autowired
    DocumentService documentService;

    public void indexDocument(String index, String type, String id, String message) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessage(message);

        IndexRequest indexRequest = new IndexRequest(index, type, id);
        indexRequest.setEntity(messageEntity);

        documentService.index(indexRequest);
    }

}
