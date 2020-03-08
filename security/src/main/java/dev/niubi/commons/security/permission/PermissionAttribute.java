package dev.niubi.commons.security.permission;

import org.springframework.security.access.ConfigAttribute;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */

public class PermissionAttribute implements ConfigAttribute {
    private final String permission;

    public PermissionAttribute(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAttribute() {
        return permission;
    }
}
