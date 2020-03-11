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
 * &#64;EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
 * &#64;Configuration
 * public class GlobalMethodConfiguration extends PermissionMethodSecurityConfiguration {
 *     private PermissionService permissionService;
 *
 *     &#64;Resource
 *     public void setPermissionService(PermissionService permissionService) {
 *         this.permissionService = permissionService;
 *     }
 *
 *     &#64;Override
 *     protected PermissionVoter permissionVoter() {
 *         return new PermissionVoter(permissionService);
 *     }
 *
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
