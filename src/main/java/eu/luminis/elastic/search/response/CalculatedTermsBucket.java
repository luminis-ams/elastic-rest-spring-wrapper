package eu.luminis.elastic.search.response;

/**
 * Created by jettrocoenradie on 19/05/2017.
 */
public class CalculatedTermsBucket extends TermsBucket {
    private ValueBucket calculated;

    public ValueBucket getCalculated() {
        return calculated;
    }

    public CalculatedTermsBucket setCalculated(ValueBucket calculated) {
        this.calculated = calculated;
        return this;
    }
}
