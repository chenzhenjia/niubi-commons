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

package dev.niubi.commons.security.captcha.mobile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

/**
 * @author chenzhenjia
 * @since 2020/6/12
 */
@Slf4j
public class MobileCaptchaAuthenticationProvider implements
    AuthenticationProvider, InitializingBean, MessageSourceAware {

  protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
  private UserCache userCache = new NullUserCache();
  private boolean forcePrincipalAsString = false;
  private UserDetailsChecker preAuthenticationChecks = new AccountStatusUserDetailsChecker();
  private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();
  private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
  private MobileUserDetailsService userDetailsService;

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(this.userDetailsService, "A MobileUserDetailsService must be set");
  }

  public void setUserDetailsService(
      MobileUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messages = new MessageSourceAccessor(messageSource);
  }

  public UserDetailsChecker getPostAuthenticationChecks() {
    return postAuthenticationChecks;
  }

  public void setPostAuthenticationChecks(
      UserDetailsChecker postAuthenticationChecks) {
    this.postAuthenticationChecks = postAuthenticationChecks;
  }

  public UserCache getUserCache() {
    return userCache;
  }

  public void setUserCache(UserCache userCache) {
    this.userCache = userCache;
  }

  public boolean isForcePrincipalAsString() {
    return forcePrincipalAsString;
  }

  public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
    this.forcePrincipalAsString = forcePrincipalAsString;
  }

  public void setAuthoritiesMapper(
      GrantedAuthoritiesMapper authoritiesMapper) {
    this.authoritiesMapper = authoritiesMapper;
  }

  public void setPreAuthenticationChecks(
      UserDetailsChecker preAuthenticationChecks) {
    this.preAuthenticationChecks = preAuthenticationChecks;
  }

  @Override
  public Authentication authenticate(
      Authentication authentication) throws AuthenticationException {
    Assert.isInstanceOf(MobileCaptchaAuthenticationToken.class, authentication,
        () -> messages.getMessage(
            "MobileCodeAuthenticationProvider.onlySupports",
            "仅支持MobileCodeAuthenticationToken"));

    MobileCaptchaAuthenticationToken mobileCodeAuthentication =
        (MobileCaptchaAuthenticationToken) authentication;
    String mobile = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
        : authentication.getName();

    UserDetails user = this.userCache.getUserFromCache(mobile);

    boolean cacheWasUsed = true;
    if (user == null) {
      cacheWasUsed = false;

      try {
        user = retrieveUser(mobile, mobileCodeAuthentication);
      } catch (UsernameNotFoundException notFound) {
        log.debug("mobile '" + mobile + "' not found");

        throw notFound;
      } catch (Exception ex) {
        throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
      }
      Assert.notNull(user,
          "retrieveUser returned null - a violation of the interface contract");
    }
    try {
      preAuthenticationChecks.check(user);
    } catch (AuthenticationException exception) {
      if (cacheWasUsed) {
        // There was a problem, so try again after checking
        // we're using latest data (i.e. not from the cache)
        cacheWasUsed = false;
        user = retrieveUser(mobile, mobileCodeAuthentication);
        preAuthenticationChecks.check(user);
      } else {
        throw exception;
      }
    }
    postAuthenticationChecks.check(user);

    if (!cacheWasUsed) {
      this.userCache.putUserInCache(user);
    }

    Object principalToReturn = user;

    if (forcePrincipalAsString) {
      principalToReturn = user.getUsername();
    }

    return createSuccessAuthentication(principalToReturn, mobileCodeAuthentication, user);
  }

  protected Authentication createSuccessAuthentication(Object principal,
      MobileCaptchaAuthenticationToken authentication, UserDetails user) {
    MobileCaptchaAuthenticationToken result = new MobileCaptchaAuthenticationToken(
        principal, authentication.getCode(),
        authoritiesMapper.mapAuthorities(user.getAuthorities()));
    result.setDetails(authentication.getDetails());

    return result;
  }

  public UserDetails retrieveUser(String mobile, MobileCaptchaAuthenticationToken authentication) {
    UserDetails user = this.userDetailsService.loadUserByMobile(mobile);
    if (user == null) {
      throw new InternalAuthenticationServiceException(
          "UserDetailsService returned null, which is an interface contract violation");
    }
    return user;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (MobileCaptchaAuthenticationToken.class
        .isAssignableFrom(authentication));
  }

  private class DefaultPostAuthenticationChecks implements UserDetailsChecker {

    public void check(UserDetails user) {
      if (!user.isCredentialsNonExpired()) {
        log.debug("User account credentials have expired");

        throw new CredentialsExpiredException(messages.getMessage(
            "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
            "User credentials have expired"));
      }
    }
  }
}
