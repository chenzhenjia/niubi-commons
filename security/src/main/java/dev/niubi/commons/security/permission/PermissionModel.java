package dev.niubi.commons.security.permission;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */
@Data
@AllArgsConstructor
public class PermissionModel {
    private String permission;
    private String tag;
}
