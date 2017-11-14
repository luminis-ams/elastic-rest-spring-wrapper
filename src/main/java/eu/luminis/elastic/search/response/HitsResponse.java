package eu.luminis.elastic.search.response;

import java.util.List;

/**
 * Response class used to return found hits together with some metadata of the query and the total available documents.
 * @param <T> Type of the Hit, beware we cannot parse results with multiple different types.
 */
public class HitsResponse<T> {
    private List<T> hits;
    private long totalHits;
    private long responseTime;
    private Boolean timedOut;

    public List<T> getHits() {
        return hits;
    }

    public void setHits(List<T> hits) {
        this.hits = hits;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public Boolean getTimedOut() {
        return timedOut;
    }

    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }
}
