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

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author chenzhenjia
 * @since 2020/6/12
 */
public class MobileCaptchaAuthenticationToken extends AbstractAuthenticationToken {

  private final Object principal;
  private final String code;

  public MobileCaptchaAuthenticationToken(Object principal, String code) {
    super(null);
    this.principal = principal;
    this.code = code;
    setAuthenticated(false);
  }

  public MobileCaptchaAuthenticationToken(Object principal, String code,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.code = code;
    super.setAuthenticated(true); // must use super, as we override
  }

  public String getCode() {
    return code;
  }

  @Override
  public Object getCredentials() {
    return "A/N";
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }
}
