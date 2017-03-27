package eu.luminis.elastic.search.response;

/**
 * Parent interface for all different type of aggregations. The only thing each aggregation has is a name.
 */
public class Aggregation {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
