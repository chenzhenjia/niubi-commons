package dev.niubi.commons.web.error;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kotlin.annotation.MustBeDocumented;

/**
 * 启用自定义的json 错误处理
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@MustBeDocumented
@Import({ResponseErrorConfiguration.class, ExceptionsHandlerRegistrar.class})
public @interface EnableResponseError {
    boolean exceptionsHandler() default true;
}
