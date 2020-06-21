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

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author chenzhenjia
 * @since 2020/6/12
 */
public class MobileCaptchaConfigurer<H extends HttpSecurityBuilder<H>> extends
  AbstractAuthenticationFilterConfigurer<H, MobileCaptchaConfigurer<H>, MobileCaptchaAuthenticationFilter> {
    MobileCaptchaAuthenticationProvider mobileCaptchaAuthenticationProvider = new MobileCaptchaAuthenticationProvider();

    public MobileCaptchaConfigurer() {
        super(new MobileCaptchaAuthenticationFilter(), "/mobile");
        mobileParameter("mobile");
        codeParameter("code");
    }

    // @Override
    // public void init(H http) throws Exception {
    //     super.init(http);
    // }

    @Override
    public void configure(H http) throws Exception {
        http.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(this.postProcess(mobileCaptchaAuthenticationProvider));

        super.configure(http);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(
      String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public MobileCaptchaConfigurer<H> mobileParameter(String mobileParameter) {
        getAuthenticationFilter().setMobileParameter(mobileParameter);
        return this;
    }

    public MobileCaptchaConfigurer<H> codeParameter(String codeParameter) {
        getAuthenticationFilter().setCodeParameter(codeParameter);
        return this;
    }

    public MobileCaptchaConfigurer<H> captchaValidator(MobileCaptchaValidator captchaValidator) {
        getAuthenticationFilter().setCaptchaValidator(captchaValidator);
        return this;
    }

    public MobileCaptchaConfigurer<H> userDetailsService(MobileUserDetailsService userDetailsService) {
        mobileCaptchaAuthenticationProvider.setUserDetailsService(userDetailsService);
        return this;
    }

}
