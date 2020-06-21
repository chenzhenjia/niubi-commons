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

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.niubi.commons.security.captcha.exception.CaptchaAuthenticationException;

/**
 * 手机号验证码授权的过滤器
 *
 * @author chenzhenjia
 * @since 2020/6/12
 */
public class MobileCaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_MOBILE_MOBILE_KEY = "mobile";
    public static final String SPRING_SECURITY_MOBILE_CODE_KEY = "code";

    public String mobileParameter = SPRING_SECURITY_MOBILE_MOBILE_KEY;
    public String codeParameter = SPRING_SECURITY_MOBILE_CODE_KEY;
    private MobileCaptchaValidator captchaValidator = new SessionMobileCaptchaValidator();

    public void setCodeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
    }

    public void setMobileParameter(String mobileParameter) {
        this.mobileParameter = mobileParameter;
    }

    public void setCaptchaValidator(
      MobileCaptchaValidator captchaValidator) {
        this.captchaValidator = captchaValidator;
    }

    public MobileCaptchaAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login/mobile", "POST"));
    }

    public MobileCaptchaAuthenticationFilter(String filterProcessesUrl) {
        super(filterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
              "Authentication method not supported: " + request.getMethod());
        }
        String mobile = obtainMobile(request);
        String code = obtainCode(request);
        if (code == null) {
            code = "";
        }
        if (mobile == null) {
            mobile = "";
        }
        boolean verify = captchaValidator.valid(request, mobile, code);
        if (!verify) {
            throw new CaptchaAuthenticationException(messages.getMessage(
              "MobileCodeAuthenticationFilter.verifyFailed",
              "手机验证码错误"));
        }


        mobile = mobile.trim();
        MobileCaptchaAuthenticationToken authRequest = new MobileCaptchaAuthenticationToken(mobile, code);
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Nullable
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    @Nullable
    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }

}
