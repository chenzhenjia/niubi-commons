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

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
 * <pre>
 * &#64;EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
 * &#64;Configuration
 * &#64;EnablePermissions
 * public class GlobalMethodConfiguration {
 *
 *      &#64;Bean
 *      public UsernamePermissionsVoter usernamePermissionsVoter(){
 *          return new UsernamePermissionsVoter();
 *      }
 * }
 * </pre>
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
public class PermissionsMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private AbstractPermissionsVoter permissionVoter;

    @Autowired
    public void setPermissionVoter(ObjectProvider<AbstractPermissionsVoter> permissionVoter) {
        this.permissionVoter = permissionVoter.getIfAvailable(DefaultPermissionsVoter::new);
    }

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return new PermissionsMethodSecurityMetadataSource();
    }

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());
        PreInvocationAuthorizationAdviceVoter adviceVoter = new PreInvocationAuthorizationAdviceVoter(expressionAdvice);

        return new AffirmativeBased(Arrays.asList(adviceVoter, new RoleVoter(), new AuthenticatedVoter(), permissionVoter));
    }

}
