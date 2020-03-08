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
