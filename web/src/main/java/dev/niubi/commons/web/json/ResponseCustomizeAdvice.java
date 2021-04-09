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

package dev.niubi.commons.web.json;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author chenzhenjia
 * @since 2019/12/11
 */
@ControllerAdvice
@RestControllerAdvice
public class ResponseCustomizeAdvice implements ResponseBodyAdvice<Object> {

  private final ResponseCustomizer responseCustomizer;

  public ResponseCustomizeAdvice(ResponseCustomizer responseCustomizer) {
    this.responseCustomizer = responseCustomizer;
  }

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    Optional<? extends Class<?>> optionalClass = Optional.ofNullable(returnType.getMethod())
        .map(Method::getReturnType);
    Boolean b = optionalClass.map(ResponseEntity.class::isAssignableFrom)
        .orElse(false);
    Boolean b1 = optionalClass
        .map(Response.class::isAssignableFrom)
        .orElse(false);
    return b1 || b;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {
    if (Objects.isNull(body)) {
      return null;
    }
    Response<?> responseBody = null;
    if (body instanceof Response) {
      responseBody = (Response<?>) body;
    }
    if (Objects.isNull(responseBody)) {
      return body;
    }
    if (responseBody instanceof SpringMvcResponse) {
      response.setStatusCode(((SpringMvcResponse<?>) responseBody).getHttpStatus());
    }
    return responseCustomizer.customize(responseBody);
  }
}
