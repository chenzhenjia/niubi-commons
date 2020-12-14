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

package dev.niubi.commons.security.captcha.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author chenzhenjia
 * @since 2020/6/12
 */
public class CaptchaAuthenticationException extends AuthenticationException {

  public CaptchaAuthenticationException(String msg, Throwable t) {
    super(msg, t);
  }

  public CaptchaAuthenticationException(String msg) {
    super(msg);
  }
}
