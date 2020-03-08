package dev.niubi.commons.security.permission;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 该对象存着在spring mvc上标注的所有的权限集合
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
public class PermissionRequestContainer implements BeanFactoryAware {
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    private List<PermissionModel> _permissions = Collections.emptyList();
    private volatile int requestMappingSize = -1;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.requestMappingHandlerMapping = beanFactory.getBean(RequestMappingHandlerMapping.class);
    }

    /**
     * 获取所有权限的集合
     */
    public List<PermissionModel> getPermissions() {
        Set<Map.Entry<RequestMappingInfo, HandlerMethod>> entries = requestMappingHandlerMapping.getHandlerMethods()
          .entrySet();
        if (entries.size() != requestMappingSize) {
            _permissions = entries
              .stream()
              .flatMap(it -> {
                  HandlerMethod value = it.getValue();
                  Class<?> clazz = value.getMethod().getDeclaringClass();
                  List<PermissionModel> methodPermission = getMethodPermission(value);
                  List<PermissionModel> classPermission = getClassPermission(clazz);
                  return Stream.concat(methodPermission.stream(), classPermission.stream());
              })
              .collect(Collectors.toList());
            requestMappingSize = entries.size();
        }
        return _permissions;
    }

    private List<PermissionModel> getMethodPermission(HandlerMethod method) {
        return Stream.of(method.getMethod().getAnnotations())
          .filter(Permission.class::isInstance)
          .map(Permission.class::cast)
          .flatMap(permission -> Stream.of(permission.value())
            .map(s -> new PermissionModel(s, permission.tag())))
          .collect(Collectors.toList());
    }

    private List<PermissionModel> getClassPermission(Class<?> clazz) {
        RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return Collections.emptyList();
        }
        return Stream.of(clazz.getAnnotations())
          .filter(Permission.class::isInstance)
          .map(Permission.class::cast)
          .flatMap(permission -> Stream.of(permission.value())
            .map(s -> new PermissionModel(s, permission.tag())))
          .collect(Collectors.toList());
    }

}
