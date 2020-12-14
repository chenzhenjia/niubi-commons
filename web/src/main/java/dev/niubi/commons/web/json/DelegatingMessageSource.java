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

package dev.niubi.commons.web.json;

import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

/**
 * @author chenzhenjia
 * @since 2020/3/31
 */
public class DelegatingMessageSource implements MessageSource {

  private final MessageSource secondMessageSource;
  private MessageSource primaryMessageSource;

  public DelegatingMessageSource(MessageSource secondMessageSource) {
    this.secondMessageSource = secondMessageSource;
  }

  public void setPrimaryMessageSource(MessageSource primaryMessageSource) {
    this.primaryMessageSource = primaryMessageSource;
  }

  @Override
  public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
    return Optional.ofNullable(primaryMessageSource)
        .map(messageSource -> messageSource.getMessage(code, args, null, locale))
        .filter(s -> !s.equals(code))
        .orElseGet(() -> secondMessageSource.getMessage(code, args, defaultMessage, locale));
  }

  @Override
  public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
    return Optional.ofNullable(primaryMessageSource)
        .map(messageSource -> messageSource.getMessage(code, args, null, locale))
        .filter(s -> !s.equals(code))
        .orElseGet(() -> secondMessageSource.getMessage(code, args, "", locale));
  }

  @Override
  public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
    return Optional.ofNullable(primaryMessageSource)
        .map(messageSource -> messageSource.getMessage(resolvable, locale))
        .orElseGet(() -> secondMessageSource.getMessage(resolvable, locale));
  }
}
