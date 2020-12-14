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

import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

/**
 * 从session 中获取需要校验的验证码
 *
 * @author chenzhenjia
 * @since 2020/6/12
 */
public class SessionImageCaptchaValidator implements ImageCaptchaValidator {

  public static final String CAPTCHA_VERIFY_ATTR = "SessionCaptchaVerifyService";
  private String captchaAttrName = CAPTCHA_VERIFY_ATTR;

  public SessionImageCaptchaValidator() {
  }

  public SessionImageCaptchaValidator(String captchaAttrName) {
    this.captchaAttrName = captchaAttrName;
  }

  public void setCaptchaAttrName(String captchaAttrName) {
    this.captchaAttrName = captchaAttrName;
  }

  @Override
  public boolean valid(HttpServletRequest request, String code) {
    Object attr = request.getSession().getAttribute(captchaAttrName);
    if (Objects.isNull(attr)) {
      return false;
    }
    return Optional.of(attr)
        .map(Object::toString)
        .map(s -> s.equals(code))
        .orElse(false);
  }
}
