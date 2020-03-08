package dev.niubi.commons.web.error;

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException;

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

import dev.niubi.commons.core.exception.BusinessException;
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

    @ExceptionHandler(MissingKotlinParameterException.class)
    public ResponseEntity<Response<?>> handleMissingKotlinParameter(MissingKotlinParameterException e) {
        Response<Object> response = Response.business(BAD_REQUEST.value(), e.getMsg());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<?>> handleBusinessException(BusinessException e) {
        log.debug("处理业务异常", e);
        Response<Object> response = Response.business(e.getCode(), e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

}
