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

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.val;

/**
 * 用户权限的投票器
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
public class PermissionVoter implements AccessDecisionVoter<MethodInvocation> {
    public final PermissionService permissionService;

    public PermissionVoter(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof PermissionAttribute;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MethodInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, MethodInvocation object, Collection<ConfigAttribute> attributes) {
        if (Objects.isNull(attributes) || Objects.isNull(authentication)) {
            return AccessDecisionVoter.ACCESS_ABSTAIN;
        }

        List<String> attrs = attributes.stream()
                .map(ConfigAttribute::getAttribute)
                .collect(Collectors.toList());
        val permissions = loadPermissions(authentication);
        boolean exists = permissions.stream()
                .anyMatch(permission -> {
                    if ("**".equals(permission)) {
                        return true;
                    }
                    return attrs.stream()
                            .anyMatch(attr -> attr.startsWith(permission) || permission.equals(attr));
                });
        if (exists) {
            return AccessDecisionVoter.ACCESS_GRANTED;
        }
        return AccessDecisionVoter.ACCESS_DENIED;
    }

    protected Set<String> loadPermissions(Authentication authentication) {
        val username = authentication.getName();
        return Optional.ofNullable(permissionService)
                .map(s -> s.loadByUsername(username))
                .orElse(Collections.emptySet());
    }

}
