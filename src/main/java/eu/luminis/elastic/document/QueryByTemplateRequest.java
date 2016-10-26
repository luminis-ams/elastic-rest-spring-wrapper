package eu.luminis.elastic.document;

import com.fasterxml.jackson.core.type.TypeReference;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jettrocoenradie on 23/08/2016.
 */
public class QueryByTemplateRequest {
    private String indexName;
    private String templateName;
    private Map<String,Object> modelParams = new HashMap<>();
    private TypeReference typeReference;
    private Boolean addId = false;

    public Boolean getAddId() {
        return addId;
    }

    public QueryByTemplateRequest setAddId(Boolean addId) {
        this.addId = addId;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public QueryByTemplateRequest setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public String getTemplateName() {
        return templateName;
    }

    public QueryByTemplateRequest setTemplateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public Map<String, Object> getModelParams() {
        return modelParams;
    }

    public QueryByTemplateRequest setModelParams(Map<String, Object> modelParams) {
        this.modelParams = modelParams;
        return this;
    }

    public QueryByTemplateRequest addModelParam(String key, Object value) {
        this.modelParams.put(key, value);
        return this;
    }

    public TypeReference getTypeReference() {
        return typeReference;
    }

    public QueryByTemplateRequest setTypeReference(TypeReference typeReference) {
        this.typeReference = typeReference;
        return this;
    }

    public String createQuery() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/" + templateName);
        JtwigModel model = JtwigModel.newModel();
        modelParams.forEach(model::with);

        return template.render(model);
    }

    public static QueryByTemplateRequest create() {
        return new QueryByTemplateRequest();
    }
}
