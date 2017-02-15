package eu.luminis.elastic.document;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * <p>This request can be used to create a request for one specific entity specified by the index, type and id.</p>
 * <p>If your entity object contains the field <code>id</code>, but you want to generate the id in elasticsearch,
 * you might want to obtain the id when executing a query and still add it to the entity object. In that case
 * you can use {@link #addId} true to indicate to copy the id into the entity object.</p>
 */
public class QueryByIdRequest extends DocumentRequest {
    private TypeReference typeReference;
    private Boolean addId = false;

    public QueryByIdRequest() {
    }

    public QueryByIdRequest(String index, String type, String id) {
        super(index, type, id);
    }

    public TypeReference getTypeReference() {
        return typeReference;
    }

    public void setTypeReference(TypeReference typeReference) {
        this.typeReference = typeReference;
    }

    public Boolean getAddId() {
        return addId;
    }

    public void setAddId(Boolean addId) {
        this.addId = addId;
    }
}
