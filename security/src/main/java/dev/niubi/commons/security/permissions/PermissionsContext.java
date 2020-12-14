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

import java.util.Set;

/**
 * 权限的上下文接口，接口只包含了权限数组，且只提供了一个默认的实现，如需要额外的功能需要自定义实现
 *
 * @author chenzhenjia
 * @since 2020/4/22
 */
public interface PermissionsContext {

  Set<String> getPermissions();

  void setPermissions(Set<String> permissions);
}
