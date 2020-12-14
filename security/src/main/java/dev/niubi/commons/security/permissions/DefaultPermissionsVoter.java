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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;

/**
 * 默认空的权限投票器，投票方法使用对比一致和startsWith来判断权限
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
public class DefaultPermissionsVoter extends AbstractPermissionsVoter {

  @Override
  protected PermissionsContext loadPermissions(Authentication authentication) {
    return new PermissionsContextImpl(Collections.emptySet());
  }

  @Override
  protected boolean vote(PermissionsContext context, List<String> attrs) {
    return Optional.ofNullable(context.getPermissions()).orElse(Collections.emptySet())
        .stream()
        .anyMatch(permission -> {
          if ("**".equals(permission)) {
            return true;
          }
          return attrs.stream()
              .anyMatch(attr -> permission.equals(attr) || attr.startsWith(permission));
        });
  }
}
