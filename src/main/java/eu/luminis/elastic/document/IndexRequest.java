package eu.luminis.elastic.document;

/**
 * Created by jettrocoenradie on 23/08/2016.
 */
public class IndexRequest {
    public enum Action {
        INDEX, CREATE, DELETE, UPDATE;
    }

    private String index;
    private String type;
    private String id;
    private Action action = Action.INDEX;
    private Object entity;
    private boolean update = false;

    public boolean isUpdate() {
        return update;
    }

    public IndexRequest setUpdate(boolean update) {
        this.update = update;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public IndexRequest setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return type;
    }

    public IndexRequest setType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public IndexRequest setId(String id) {
        this.id = id;
        return this;
    }

    public Object getEntity() {
        return entity;
    }

    public IndexRequest setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    public String action() {
        return this.action.name().toLowerCase();
    }

    public IndexRequest setAction(Action action) {
        this.action = action;
        return this;
    }

    public IndexRequest create() {
        return setAction(Action.CREATE);
    }

    public IndexRequest index() {
        return setAction(Action.INDEX);
    }

    public IndexRequest update() {
        return setAction(Action.UPDATE);
    }

    public IndexRequest delete() {
        return setAction(Action.DELETE);
    }
}
