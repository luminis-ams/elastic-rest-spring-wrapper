package eu.luminis.elastic;

/**
 * Class to contain the supported request methods as constants.
 */
public class RequestMethod {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    private RequestMethod() {
        throw new IllegalStateException("Utility class");
    }
}
