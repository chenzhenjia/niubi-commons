package dev.niubi.commons.security.permission;

import java.util.Set;

/**
 * 加载用户所属权限的接口
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