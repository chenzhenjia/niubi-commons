package dev.niubi.commons.security.permission;

import org.springframework.core.BridgeMethodResolver;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.val;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */
public class PermissionMethodSecurityMetadataSource extends AbstractFallbackMethodSecurityMetadataSource {
    @Override
    protected Collection<ConfigAttribute> findAttributes(Method method, Class<?> targetClass) {
        return processAnnotations(BridgeMethodResolver.findBridgedMethod(method).getAnnotations());
    }

    @Override
    protected Collection<ConfigAttribute> findAttributes(Class<?> clazz) {
        return processAnnotations(clazz.getAnnotations());
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    private List<ConfigAttribute> processAnnotations(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof Permission) {
                val attributes = new ArrayList<ConfigAttribute>();
                Permission permission = (Permission) annotation;
                for (String value : permission.value()) {
                    attributes.add(new PermissionAttribute(value));
                }
                return attributes;
            }
        }
        return null;
    }

}
