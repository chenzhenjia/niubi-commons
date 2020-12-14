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

package dev.niubi.commons.security.captcha.image;

import dev.niubi.commons.security.captcha.exception.CaptchaAuthenticationException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

/**
 * 图片验证码验证拦截器
 *
 * @author chenzhenjia
 * @since 2020/6/12
 */
public class ImageCaptchaVerifyFilter extends GenericFilterBean {

  public static final String SPRING_SECURITY_FORM_VERIFY_CODE_KEY = "captcha";
  protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
  private String verifyCodeParameter = SPRING_SECURITY_FORM_VERIFY_CODE_KEY;
  private RequestMatcher requiresAuthenticationRequestMatcher;
  private ImageCaptchaValidator captchaValidator;
  private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

  public ImageCaptchaVerifyFilter() {
    requiresAuthenticationRequestMatcher = new AntPathRequestMatcher("/login", "POST");
    captchaValidator = new SessionImageCaptchaValidator();
  }

  public void setVerifyCodeParameter(String verifyCodeParameter) {
    Assert.hasText(verifyCodeParameter, "verifyCode parameter must not be empty or null");
    this.verifyCodeParameter = verifyCodeParameter;
  }

  public void setFailureHandler(
      AuthenticationFailureHandler failureHandler) {
    this.failureHandler = failureHandler;
  }

  @Nullable
  protected String obtainVerifyCode(HttpServletRequest request) {
    return request.getParameter(verifyCodeParameter);
  }

  public void setCaptchaValidator(
      ImageCaptchaValidator captchaValidator) {
    this.captchaValidator = captchaValidator;
  }

  public final void setRequiresAuthenticationRequestMatcher(
      RequestMatcher requestMatcher) {
    Assert.notNull(requestMatcher, "requestMatcher cannot be null");
    this.requiresAuthenticationRequestMatcher = requestMatcher;
  }

  protected boolean requiresAuthentication(HttpServletRequest request,
      HttpServletResponse response) {
    return requiresAuthenticationRequestMatcher.matches(request);
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res,
      FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (!requiresAuthentication(request, response)) {
      chain.doFilter(request, response);

      return;
    }
    String verifyCode = obtainVerifyCode(request);
    if (verifyCode == null) {
      verifyCode = "";
    }
    try {
      boolean verify = captchaValidator.valid(request, verifyCode);
      if (!verify) {
        throw new CaptchaAuthenticationException(messages.getMessage(
            "CaptchaUsernamePasswordAuthenticationFilter.verifyFailed",
            "验证码错误"));
      }
      chain.doFilter(request, response);
    } catch (CaptchaAuthenticationException e) {
      unsuccessfulAuthentication(request, response, e);
    }
  }

  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();

    if (logger.isDebugEnabled()) {
      logger.debug("Cleared security context due to exception", failed);
    }
    request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, failed);

    if (failureHandler != null) {
      failureHandler.onAuthenticationFailure(request, response, failed);
    }
  }
}
