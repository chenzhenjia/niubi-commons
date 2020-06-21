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

import javax.servlet.http.HttpServletRequest;

import dev.niubi.commons.security.captcha.exception.CaptchaAuthenticationException;

/**
 * 图片验证码验证器
 *
 * @author chenzhenjia
 * @since 2020/6/12
 */
@FunctionalInterface
public interface ImageCaptchaValidator {
    /**
     * 验证用户输入的验证码是否正确
     *
     * @param request 当前请求
     * @param code    用户输入的验证码
     */
    boolean valid(HttpServletRequest request, String code) throws CaptchaAuthenticationException;

}
