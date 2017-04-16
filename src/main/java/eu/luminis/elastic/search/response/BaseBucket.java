package eu.luminis.elastic.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jettrocoenradie on 15/04/2017.
 */
public class BaseBucket extends Bucket {
    private String key;

    @JsonProperty("doc_count")
    private Long docCount;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

}
