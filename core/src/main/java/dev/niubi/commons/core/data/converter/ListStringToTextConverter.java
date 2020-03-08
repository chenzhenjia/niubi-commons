package dev.niubi.commons.core.data.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.persistence.AttributeConverter;

import lombok.extern.slf4j.Slf4j;


/**
 * 自定义 hibernate 的 String 转 List<String> 的 converter,对应数据库的 varchar 类型
 * <pre>
 * {@code
 * @Entity
 * public class ExampleEntity {
 *     @Convert(converter = ListStringToTextConverter.class)
 *     private Type type;
 * }
 * }
 * </pre>
 *
 * @author chenzhenjia
 * @since 2019/11/23
 */
@Slf4j
public class ListStringToTextConverter implements AttributeConverter<List<String>, String>, Serializable {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (Objects.isNull(attribute)) return null;
        try {
            return OBJECT_MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.debug("list convert to json string error", e);
            return null;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData)) return null;
        try {
            return OBJECT_MAPPER.readValue(dbData, new TypeReference<List<String>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
        } catch (IOException e) {
            log.debug("json string convert to list error", e);
            return null;
        }
    }
}
