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

package dev.niubi.commons.web.error;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import dev.niubi.commons.web.error.exception.BusinessException;
import dev.niubi.commons.web.json.Response;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */
@ControllerAdvice
@Slf4j
public class ExceptionsHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBindingErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> map = ex.getBindingResult().getFieldErrors()
          .stream()
          .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()))
          .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

        Map<String, List<String>> objectErrorMap = ex.getBindingResult().getGlobalErrors()
          .stream()
          .filter(objectError -> Objects.nonNull(objectError.getDefaultMessage()))
          .collect(Collectors.groupingBy(ObjectError::getObjectName, Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));


        map.putAll(objectErrorMap);

        String msg = map.values().stream().findFirst()
          .map(o -> {
              if (o instanceof List) {
                  if (((List) o).size() > 0) {
                      return ((List) o).get(0).toString();
                  }
              }
              return o.toString();
          })
          .orElse(null);
        Response<Object> response = Response.business(BAD_REQUEST.value(), msg);
        response.putAllExtra(map);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<?>> handleBusinessException(BusinessException e) {
        log.debug("处理业务异常", e);
        Response<Object> response = Response.business(e.getCode(), e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

}
