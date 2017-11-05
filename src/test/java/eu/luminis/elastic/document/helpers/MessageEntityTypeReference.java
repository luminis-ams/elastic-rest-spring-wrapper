package eu.luminis.elastic.document.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.luminis.elastic.search.response.QueryResponse;

/**
 * Type reference required to make use of Jackson and nested entities.
 */
public class MessageEntityTypeReference extends TypeReference<QueryResponse<MessageEntity>> {
}
