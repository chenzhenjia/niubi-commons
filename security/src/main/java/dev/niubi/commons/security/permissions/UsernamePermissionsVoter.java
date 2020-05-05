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

import org.springframework.security.core.Authentication;

import java.util.Objects;
import java.util.function.Function;

/**
 * 根据用户名加载权限数据
 * <pre>
 * &#64;EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
 * &#64;Configuration
 * &#64;EnablePermissions
 * public class GlobalMethodConfiguration {
 *
 *      &#64;Bean
 *      public UsernamePermissionsVoter usernamePermissionsVoter(){
 *          return new UsernamePermissionsVoter(username -> {
 *             return new PermissionsContextImpl(Collections.emptySet());
 *         });
 *      }
 * }
 * </pre>
 * @author chenzhenjia
 * @since 2020/4/24
 */
public class UsernamePermissionsVoter extends DefaultPermissionsVoter {
    private Function<String, PermissionsContext> loadContext;

    public UsernamePermissionsVoter() {
    }

    public UsernamePermissionsVoter(Function<String, PermissionsContext> loadContext) {
        this.loadContext = loadContext;
    }

    public void setUsernamePermissionsLoader(Function<String, PermissionsContext> loadContext) {
        this.loadContext = loadContext;
    }

    @Override
    protected PermissionsContext loadPermissions(Authentication authentication) {
        String name = authentication.getName();
        if (Objects.isNull(loadContext)) {
            return new PermissionsContextImpl();
        }
        return loadContext.apply(name);
    }
}
