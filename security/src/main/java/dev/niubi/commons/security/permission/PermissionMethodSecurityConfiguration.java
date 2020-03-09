package dev.niubi.commons.security.permission;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.Arrays;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 * <pre>
 * {@code
 * @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
 * @Configuration
 * public class GlobalMethodConfiguration extends PermissionMethodSecurityConfiguration {
 *     private PermissionService permissionService;
 *
 *     @Resource
 *     public void setPermissionService(PermissionService permissionService) {
 *         this.permissionService = permissionService;
 *     }
 *
 *     @Override
 *     protected PermissionVoter permissionVoter() {
 *         return new PermissionVoter(permissionService);
 *     }
 *
 * }
 * }
 * </pre>
 */
public abstract class PermissionMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return new PermissionMethodSecurityMetadataSource();
    }

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());
        PreInvocationAuthorizationAdviceVoter adviceVoter = new PreInvocationAuthorizationAdviceVoter(expressionAdvice);

        return new AffirmativeBased(Arrays.asList(adviceVoter, new RoleVoter(), new AuthenticatedVoter(), permissionVoter()));
    }

    protected abstract PermissionVoter permissionVoter();

}
