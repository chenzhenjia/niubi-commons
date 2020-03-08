package dev.niubi.commons.security.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
public @interface Permission {
    /**
     * 权限的 key
     */
    String[] value();

    /**
     * 权限的 tag 名字
     */
    String tag();
}
