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

import dev.niubi.commons.web.error.exception.BusinessException;
import dev.niubi.commons.web.json.Response;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */
@ControllerAdvice
@Slf4j
public class ExceptionsHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public Response<?> handleBindingErrors(MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    return bindingResultResponseMessage(bindingResult);
  }

  protected Response<?> bindingResultResponseMessage(BindingResult bindingResult) {
    Map<String, List<String>> map = bindingResult.getFieldErrors()
        .stream()
        .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()))
        .collect(Collectors.groupingBy(FieldError::getField,
            Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));

    Map<String, List<String>> objectErrorMap = bindingResult.getGlobalErrors()
        .stream()
        .filter(objectError -> Objects.nonNull(objectError.getDefaultMessage()))
        .collect(Collectors.groupingBy(ObjectError::getObjectName,
            Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage,
                Collectors.toList())));

    map.putAll(objectErrorMap);

    String msg = map.values().stream().findFirst()
        .map(o -> {
          if (o instanceof List) {
            if (o.size() > 0) {
              return o.get(0);
            }
          }
          return o.toString();
        })
        .orElse(null);
    return Response.business(msg).status(BAD_REQUEST)
        .extra(map).build();
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseBody
  public Response<Object> handleBusinessException(BusinessException ex) {
    log.debug("处理业务异常", ex);
    return Response.business(ex.getCode()).msg(ex.getMessage())
        .status(ex.getStatus()).build();
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseBody
  public Response<Object> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {

    return Response.business("{ExceptionsHandler.MissingServletRequestParameterException}")
        .status(BAD_REQUEST)
        .extra("exception", ex.getMessage()).build();
  }

  @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class})
  @ResponseBody
  public Response<Object> handleHttpMessageNotReadableException(RuntimeException ex) {

    return Response.business("{ExceptionsHandler.HttpMessageNotReadable}")
        .status(BAD_REQUEST).extra("exception", ex.getMessage()).build();
  }

  @ExceptionHandler(BindException.class)
  @ResponseBody
  public Response<?> handleBindException(BindException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    return bindingResultResponseMessage(bindingResult);
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseBody
  public Response<Object> handleValidationException(ValidationException ex) {
    Response<Object> response;
    if (ex instanceof ConstraintViolationException) {
      ConstraintViolationException cex = (ConstraintViolationException) ex;
      Set<ConstraintViolation<?>> violations = cex.getConstraintViolations();
      Map<String, String> errorMap = violations.stream()
          .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage));
      String msg = errorMap.values().stream().findFirst().orElse(null);
      response = Response.business(msg).status(BAD_REQUEST).extra(errorMap).build();
      return response;
    } else {
      response = Response.business("{ExceptionsHandler.ValidationException}").status(BAD_REQUEST)
          .extra("exception", ex.getMessage()).build();
    }
    return response;
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseBody
  public Response<Object> noHandlerFoundException(NoHandlerFoundException ex) {
    return Response.notfound().extra("exception", ex.getMessage()).build();
  }
}
