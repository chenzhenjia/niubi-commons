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

package dev.niubi.commons.web.json;

import dev.niubi.commons.web.json.i18n.ResponseMessageCodeFormatter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
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
  private final ResponseMessageCodeFormatter messageCodeFormatter;

  public ResponseCustomizeAdvice(ResponseCustomizer responseCustomizer,
      ResponseMessageCodeFormatter messageCodeFormatter) {
    this.responseCustomizer = responseCustomizer;
    this.messageCodeFormatter = messageCodeFormatter;
  }

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return Optional.ofNullable(returnType.getMethod())
        .map(Method::getReturnType)
        .map(c -> c.isAssignableFrom(Response.class))
        .orElse(false);
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {
    if (Objects.nonNull(body) && body instanceof Response) {
      Response<?> responseBody = (Response<?>) body;
      if (responseBody instanceof SpringMvcResponse) {
        SpringMvcResponse<?> springMvcResponse = (SpringMvcResponse<?>) responseBody;
        response.setStatusCode(springMvcResponse.getHttpStatus());
        springMvcResponse.setTimestamp(new Date());
        if (springMvcResponse.isI18n()) {
          responseBody.setMsg(messageCodeFormatter.getMsg(responseBody.getMsg()));
        }
      }
      return responseCustomizer.customize(responseBody);
    }
    return body;
  }
}
