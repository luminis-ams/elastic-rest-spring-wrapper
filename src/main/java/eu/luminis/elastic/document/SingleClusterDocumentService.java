package eu.luminis.elastic.document;

import static eu.luminis.elastic.SingleClusterRestClientFactoryBean.DEFAULT_CLUSTER_NAME;

/**
 * Wrapper for the {@code DocumentService} without the option to work with multiple clusters. Only the default cluster
 * is available.
 */
public class SingleClusterDocumentService {
    private DocumentService documentService;

    /**
     * Default constructor
     *
     * @param documentService Wrapped document service
     */
    public SingleClusterDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * By specifying the unique identification of an object we can return only that object. If we cannot find the object
     * we throw an {@link QueryByIdNotFoundException}.
     *
     * @param request Object containing the required parameters
     * @param <T>     Type of the object to be mapped to
     * @return Found object of type T
     */
    public <T> T queryById(QueryByIdRequest request) {
        return documentService.queryById(DEFAULT_CLUSTER_NAME, request);
    }

    /**
     * Checks if the provided document exists or not. If we can find the document true is returned, else false
     *
     * @param request ExistsRequest that must contain an index, type and id to check if the document exists
     * @return Boolean true if the document exists, false otherwise
     */
    public Boolean exists(ExistsRequest request) {
        return documentService.exists(DEFAULT_CLUSTER_NAME, request);
    }

    /**
     * Index the provided document using the provided parameters. If an id is provided we do an update, of no id is
     * provided we do an insert and we return the id.
     *
     * @param request Object containing the required parameters
     * @return Generated ID
     */
    public String index(IndexRequest request) {
        return documentService.index(DEFAULT_CLUSTER_NAME, request);
    }

    /**
     * Throws an exception if the provided request contains an id that already exists.
     *
     * @param request IndexRequest containing the entity to index
     * @return The id of the object, which is the same as the one you have provided.
     */
    public String create(IndexRequest request) {
        return documentService.create(DEFAULT_CLUSTER_NAME, request);
    }

    /**
     * Removes the document with the provided unique identification.
     *
     * @param request Request object containing the required parameters
     * @return Message line that can be used to see if we succeeded.
     */
    public String remove(DeleteRequest request) {
        return this.documentService.remove(DEFAULT_CLUSTER_NAME, request);
    }

    /**
     * Updates the document as specified in the provided UpdateRequest.
     *
     * @param request UpdateRequest containing the required information to execute an update
     * @return The id of the updated document
     */
    public String update(UpdateRequest request) {
        return this.documentService.update(DEFAULT_CLUSTER_NAME, request);
    }
}
