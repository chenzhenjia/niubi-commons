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

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Objects;

/**
 * 图片验证码验证的SpringSecurity配置
 *
 * @author chenzhenjia
 * @since 2020/6/12
 */
public class ImageCaptchaVerifyConfigurer<H extends HttpSecurityBuilder<H>>
  extends AbstractHttpConfigurer<ImageCaptchaVerifyConfigurer<H>, H> {
    private final ImageCaptchaVerifyFilter imageCaptchaVerifyFilter = new ImageCaptchaVerifyFilter();
    private String loginProcessingUrl = "/login";

    /**
     * 设置验证码参数的名字
     */
    public ImageCaptchaVerifyConfigurer<H> verifyCodeParameter(String verifyCodeParameter) {
        imageCaptchaVerifyFilter.setVerifyCodeParameter(verifyCodeParameter);
        return this;
    }

    /**
     * 设置验证验证码是否正确接口
     */
    public ImageCaptchaVerifyConfigurer<H> captchaValidator(ImageCaptchaValidator captchaValidator) {
        imageCaptchaVerifyFilter.setCaptchaValidator(captchaValidator);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configure(H builder) throws Exception {
        FormLoginConfigurer<H> configurer = builder.getConfigurer(FormLoginConfigurer.class);
        if (Objects.isNull(configurer)) {
            return;
        }
        configurer.loginProcessingUrl(loginProcessingUrl);
        imageCaptchaVerifyFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(
          loginProcessingUrl, "POST"));
        builder.addFilterBefore(imageCaptchaVerifyFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 注意需要和FormLogin登录的地址完全一样
     *
     * @param loginProcessingUrl formLogin登录的地址
     */
    public ImageCaptchaVerifyConfigurer<H> loginProcessingUrl(String loginProcessingUrl) {
        this.loginProcessingUrl = loginProcessingUrl;
        return this;
    }

    public ImageCaptchaVerifyConfigurer<H> failureHandler(AuthenticationFailureHandler failureHandler) {
        this.imageCaptchaVerifyFilter.setFailureHandler(failureHandler);
        return this;
    }

}
