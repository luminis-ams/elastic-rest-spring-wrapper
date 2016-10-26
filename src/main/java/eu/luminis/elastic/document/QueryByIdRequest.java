package eu.luminis.elastic.document;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Created by jettrocoenradie on 23/08/2016.
 */
public class QueryByIdRequest {
    private String index;
    private String type;
    private String id;
    private TypeReference typeReference;
    private Boolean addId = false;

    public String getIndex() {
        return index;
    }

    public QueryByIdRequest setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return type;
    }

    public QueryByIdRequest setType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public QueryByIdRequest setId(String id) {
        this.id = id;
        return this;
    }

    public TypeReference getTypeReference() {
        return typeReference;
    }

    public QueryByIdRequest setTypeReference(TypeReference typeReference) {
        this.typeReference = typeReference;
        return this;
    }

    public Boolean getAddId() {
        return addId;
    }

    public QueryByIdRequest setAddId(Boolean addId) {
        this.addId = addId;
        return this;
    }

    public static QueryByIdRequest create() {
        return new QueryByIdRequest();
    }

}
