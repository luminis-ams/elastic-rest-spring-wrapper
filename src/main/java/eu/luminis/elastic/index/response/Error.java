package eu.luminis.elastic.index.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    @JsonProperty(value = "reason")
    private String reason;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "caused_by")
    private CausedBy causedBy;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CausedBy getCausedBy() {
        return causedBy;
    }

    public void setCausedBy(CausedBy causedBy) {
        this.causedBy = causedBy;
    }

    @Override
    public String toString() {
        return "Error{" +
                "reason='" + reason + '\'' +
                ", type='" + type + '\'' +
                ", causedBy=" + causedBy +
                '}';
    }
}
