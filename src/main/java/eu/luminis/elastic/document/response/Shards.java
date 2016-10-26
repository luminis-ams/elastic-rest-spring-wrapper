package eu.luminis.elastic.document.response;

/**
 * Created by jettrocoenradie on 08/07/2016.
 */
public class Shards {
    private Integer total;

    private Integer successful;

    private Integer failed;

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
}
