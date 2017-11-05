package eu.luminis.elastic.search.response;

/**
 * Created by jettrocoenradie on 19/05/2017.
 */
public class ValueBucket {
    private double value;

    public double getValue() {
        return value;
    }

    public ValueBucket setValue(double value) {
        this.value = value;
        return this;
    }
}
