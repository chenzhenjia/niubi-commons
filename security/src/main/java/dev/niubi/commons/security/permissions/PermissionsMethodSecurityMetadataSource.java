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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.val;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */
public class PermissionsMethodSecurityMetadataSource extends AbstractFallbackMethodSecurityMetadataSource {

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
        String value = permission.value();
        attributes.add(new PermissionAttribute(value, permission.tag()));
        return attributes;
      }
    }
    return null;
  }
}
