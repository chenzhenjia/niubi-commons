package dev.niubi.commons.web.json;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * @author chenzhenjia
 * @since 2019/12/11
 */
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    private final ResponseCustomizer responseCustomizer;

    public ResponseAdvice(ResponseCustomizer responseCustomizer) {
        this.responseCustomizer = responseCustomizer;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Optional.ofNullable(returnType.getMethod())
                .map(Method::getReturnType)
                .map(c -> c.isAssignableFrom(Response.class))
                .orElse(false);

    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (Objects.nonNull(body) && body instanceof Response) {
            return responseCustomizer.customize((Response<?>) body);
        }
        return body;
    }
}
