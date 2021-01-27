/*
 * Copyright 2021 陈圳佳
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

package dev.niubi.commons.web.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException;
import dev.niubi.commons.web.json.Response;
import dev.niubi.commons.web.json.i18n.ResponseMessageCodeFormatter;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenzhenjia
 * @since 2020/3/24
 */
@ControllerAdvice
@Slf4j
public class KotlinExceptionHandler {

  private final ResponseMessageCodeFormatter responseMessageCodeFormatter;

  public KotlinExceptionHandler(ResponseMessageCodeFormatter responseMessageCodeFormatter) {
    this.responseMessageCodeFormatter = responseMessageCodeFormatter;
  }

  @ExceptionHandler(MissingKotlinParameterException.class)
  @ResponseBody
  public Response<Object> handleMissingKotlinParameter(MissingKotlinParameterException e) {
    List<JsonMappingException.Reference> path = e.getPath();
    String nullFieldName = path.stream().findFirst()
        .map(JsonMappingException.Reference::getFieldName)
        .orElse(null);
    String notNull = responseMessageCodeFormatter.getMsg("{KotlinExceptionHandler.notNull}");
    String msg = String.format("[%s]%s", nullFieldName, notNull);
    Map<String, String> extra = path.stream()
        .map(JsonMappingException.Reference::getFieldName)
        .filter(Objects::nonNull)
        .map(s -> {
          return new AbstractMap.SimpleEntry<>(s, notNull);
        })
        .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey,
            AbstractMap.SimpleEntry::getValue));
    return Response.business(msg)
        .extra(extra)
        .status(BAD_REQUEST).build();
  }
}
