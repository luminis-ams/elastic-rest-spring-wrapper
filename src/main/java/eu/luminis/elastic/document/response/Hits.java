package eu.luminis.elastic.document.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by jettrocoenradie on 08/07/2016.
 */
public class Hits<T> {
    private List<Hit<T>> hits;
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
