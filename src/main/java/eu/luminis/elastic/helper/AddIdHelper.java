package eu.luminis.elastic.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Contains methods that have a utility characteristic for this library
 */
public class AddIdHelper {
    private static final Logger logger = LoggerFactory.getLogger(AddIdHelper.class);

    private AddIdHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Uses the provided id to call the methods <code>setId(String id)</code> of the provided entity instance
     *
     * @param id     String containing the id to set
     * @param source Entity object that contains the <code>setId</code> method
     * @param <T>    Type of the provided entity object
     */
    public static <T> void addIdToEntity(String id, T source) {
        addIdToEntity(id, source, "setId");
    }

    /**
     * Uses the provided id to call the methods with the provided name of the provided entity instance
     *
     * @param id     String containing the id to set
     * @param source Entity object that contains the <code>setId</code> method
     * @param <T>    Type of the provided entity object
     */
    public static <T> void addIdToEntity(String id, T source, String methodName) {
        Method setIdMethod;
        try {
            setIdMethod = source.getClass().getMethod(methodName, String.class);
            setIdMethod.invoke(source, id);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            String message = String.format("The provided method '%s' is not available.", methodName);
            logger.warn(message);
            logger.debug(e.getMessage());
            throw new HelperException(message);
        } catch (IllegalAccessException e) {
            String message = String.format("Id argument '%s' seems to be wrong", id);
            logger.warn(message, e);
            throw new HelperException("Id argument seems to be wrong");
        }

    }
}
