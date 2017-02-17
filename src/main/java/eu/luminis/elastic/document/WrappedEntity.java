package eu.luminis.elastic.document;

public class WrappedEntity {

    private Object doc;

    public WrappedEntity(Object entity) {
        this.doc = entity;
    }

    public Object getDoc() {
        return doc;
    }

    public void setDoc(Object doc) {
        this.doc = doc;
    }
}
