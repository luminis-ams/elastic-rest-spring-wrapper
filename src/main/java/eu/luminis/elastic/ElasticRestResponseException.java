package eu.luminis.elastic;

import org.apache.http.StatusLine;

public abstract class ElasticRestResponseException extends RuntimeException {
    private final Status status;

    public enum Status {
        NO_RESPONSE,
        SERVER_ERROR,
        NOT_FOUND,
        BAD_REQUEST,
        CLIENT_ERROR,
        UNKNOWN;

        public static Status from(StatusLine statusLine) {
            if (statusLine == null) {
                return NO_RESPONSE;
            }
            int code = statusLine.getStatusCode();
            if (code == 400) {
                return BAD_REQUEST;
            } else if (code == 404) {
                return NOT_FOUND;
            } else if (code > 400 && code <= 500) {
                return CLIENT_ERROR;
            } else if (code >= 500) {
                return SERVER_ERROR;
            }
            return  UNKNOWN;
        }

    }

    public ElasticRestResponseException(StatusLine statusLine) {
        this(statusLine.getReasonPhrase(), Status.from(statusLine));
    }

    public ElasticRestResponseException(String message, Status status) {
        super(message);
        this.status = status;
    }


    public ElasticRestResponseException(String message) {
        super(message);
        this.status = Status.UNKNOWN;
    }

    public ElasticRestResponseException(String message, Throwable cause) {
        super(message, cause);
        this.status = Status.UNKNOWN;
    }

    public ElasticRestResponseException(Status status, Exception e) {
        super(e);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

}
