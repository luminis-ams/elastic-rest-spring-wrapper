package eu.luminis.elastic.document.response;

/**
 * Object to represent the shards part of the elasticsearch response.
 */
public class Shards {
    private Integer total;

    private Integer successful;

    private Integer failed;

    private Integer skipped;

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    public Integer getSuccessful() {
        return successful;
    }

    public void setSuccessful(Integer successful) {
        this.successful = successful;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSkipped() {
        return skipped;
    }

    public void setSkipped(Integer skipped) {
        this.skipped = skipped;
    }
}
