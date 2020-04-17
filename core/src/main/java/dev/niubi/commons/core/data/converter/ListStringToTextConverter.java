/*
 * Copyright 2020 陈圳佳
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * &#64;Entity
 * public class ExampleEntity {
 *     &#64;Convert(converter = ListStringToTextConverter.class)
 *     private Type type;
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
