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
import java.util.Set;

/**
 * 权限上下文的默认实现类
 *
 * @author chenzhenjia
 * @since 2020/4/23
 */
public class PermissionsContextImpl implements PermissionsContext {

  private Set<String> permissions;

  public PermissionsContextImpl() {
    this.permissions = Collections.emptySet();
  }

  public PermissionsContextImpl(Set<String> permissions) {
    this.permissions = permissions;
  }

  @Override
  public Set<String> getPermissions() {
    return permissions;
  }

  @Override
  public void setPermissions(Set<String> permissions) {
    this.permissions = permissions;
  }
}
