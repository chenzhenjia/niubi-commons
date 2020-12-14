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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

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
        BindingResult bindingResult = ex.getBindingResult();
        return ResponseEntity.badRequest().body(bindingResultResponseMessage(bindingResult));
    }

    protected Response<?> bindingResultResponseMessage(BindingResult bindingResult) {
        Map<String, Object> map = bindingResult.getFieldErrors()
          .stream()
          .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()))
          .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

        Map<String, List<String>> objectErrorMap = bindingResult.getGlobalErrors()
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
        return Response.business(msg).status(BAD_REQUEST)
          .extra(map).build();
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<?>> handleBusinessException(BusinessException ex) {
        log.debug("处理业务异常", ex);
        Response<Object> response = Response.business(ex.getCode()).msg(ex.getMessage())
          .status(ex.getStatus()).build();
        HttpStatus status = ex.getStatus();
        if (Objects.isNull(status)) {
            status = HttpStatus.OK;
        }
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Response<?>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        Response<Object> response = Response.business("ExceptionsHandler.MissingServletRequestParameterException")
          .i18n().status(BAD_REQUEST)
          .extra("exception", ex.getMessage()).build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<Response<?>> handleHttpMessageNotReadableException(RuntimeException ex) {
        Response<Object> response = Response.business("ExceptionsHandler.HttpMessageNotReadable")
          .i18n().status(BAD_REQUEST).extra("exception", ex.getMessage()).build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response<?>> handleBindException(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return ResponseEntity.badRequest().body(bindingResultResponseMessage(bindingResult));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Response<?>> handleValidationException(ValidationException ex) {
        Response<Object> response;
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException cex = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> violations = cex.getConstraintViolations();
            Map<String, String> errorMap = violations.stream()
              .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage));
            String msg = errorMap.values().stream().findFirst().orElse(null);
            response = Response.business(msg).status(BAD_REQUEST).extra(errorMap).build();
            return ResponseEntity.badRequest().body(response);
        } else {
            response = Response.business("ExceptionsHandler.ValidationException").status(BAD_REQUEST)
              .i18n().extra("exception", ex.getMessage()).build();
        }
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Response<?>> noHandlerFoundException(NoHandlerFoundException ex) {
        Response<Object> response = Response.notfound().extra("exception", ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
