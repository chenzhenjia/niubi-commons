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

package dev.niubi.commons.security.permission;

import java.util.Set;

/**
 * 加载用户所属权限的接口
 * <pre>
 * {@code
 * @Component
 * public class CachePermissionService implements PermissionService {
 *     @Override
 *     @Transactional(readOnly = true)
 *     @Cacheable(value = "permission", key = "#username")
 *     public Set<String> loadByUsername(String username) {
 *         // query database load user permission
 *         return Collections.emptySet();
 *     }
 * }
 * }
 * </pre>
 *
 * @author chenzhenjia
 * @since 2019-07-13
 */
@FunctionalInterface
public interface PermissionService {
    /**
     * 根据用户名查询用户权限集合
     *
     * @param username 用户名
     * @return 用户权限
     */
    Set<String> loadByUsername(String username);

}