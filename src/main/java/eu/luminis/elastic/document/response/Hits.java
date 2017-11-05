package eu.luminis.elastic.document.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Following the structure of an elasticsearch response. The hits object contains information about the total set
 * of hits available and it contains the first hits that are returned.
 */
public class Hits<T> {

    private List<Hit<T>> hits;

    @JsonProperty("total")
    private Long total;

    @JsonProperty("max_score")
    private Double maxScore;

    public List<Hit<T>> getHits() {
        return hits;
    }

    public void setHits(List<Hit<T>> hits) {
        this.hits = hits;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }
}
