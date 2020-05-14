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

package dev.niubi.commons.security.permissions;

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
    private List<PermissionAttribute> _permissions = Collections.emptyList();
    private volatile int requestMappingSize = -1;
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 获取所有权限的集合
     */
    public List<PermissionAttribute> getPermissions() {
        RequestMappingHandlerMapping handlerMapping = beanFactory.getBean(RequestMappingHandlerMapping.class);
        Set<Map.Entry<RequestMappingInfo, HandlerMethod>> entries = handlerMapping.getHandlerMethods()
          .entrySet();
        if (entries.size() != requestMappingSize) {
            _permissions = entries
              .stream()
              .flatMap(it -> {
                  HandlerMethod value = it.getValue();
                  Class<?> clazz = value.getMethod().getDeclaringClass();
                  List<PermissionAttribute> methodPermission = getMethodPermission(value);
                  List<PermissionAttribute> classPermission = getClassPermission(clazz);
                  return Stream.concat(methodPermission.stream(), classPermission.stream());
              })
              .collect(Collectors.toList());
            requestMappingSize = entries.size();
        }
        return _permissions;
    }

    private List<PermissionAttribute> getMethodPermission(HandlerMethod method) {
        return Stream.of(method.getMethod().getAnnotations())
          .filter(Permission.class::isInstance)
          .map(Permission.class::cast)
          .map(permission -> new PermissionAttribute(permission.value(), permission.tag()))
          .collect(Collectors.toList());
    }

    private List<PermissionAttribute> getClassPermission(Class<?> clazz) {
        RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return Collections.emptyList();
        }
        return Stream.of(clazz.getAnnotations())
          .filter(Permission.class::isInstance)
          .map(Permission.class::cast)
          .flatMap(permission -> Stream.of(permission.value())
            .map(s -> new PermissionAttribute(s, permission.tag())))
          .collect(Collectors.toList());
    }

}
