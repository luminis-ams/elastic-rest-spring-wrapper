package eu.luminis.elastic.document;

public enum Refresh {
    NOW("true"),
    WAIT("wait_for"),
    NONE("false");

    private String name;

    Refresh(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
