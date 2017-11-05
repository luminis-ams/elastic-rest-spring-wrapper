package eu.luminis.elastic.document.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.luminis.elastic.document.response.GetByIdResponse;

/**
 * Type reference required to make use of Jackson and nested entities.
 */
public class MessageEntityByIdTypeReference extends TypeReference<GetByIdResponse<MessageEntity>>{
}
