package eu.luminis.elastic.index.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CausedBy {

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "reason")
    private String reason;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "CausedBy{" +
                "type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
