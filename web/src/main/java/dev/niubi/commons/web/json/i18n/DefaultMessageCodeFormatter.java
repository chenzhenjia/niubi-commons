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

package dev.niubi.commons.web.json.i18n;

import java.util.Objects;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author chenzhenjia
 * @since 2020/4/12
 */
public class DefaultMessageCodeFormatter implements ResponseMessageCodeFormatter {

  public static final String NOT_FOUND_MESSAGE = "AbstractResponseMsgFormatter.NotFound";
  private final MessageSource messageSource;

  public DefaultMessageCodeFormatter(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public String getMsg(String msg) {
    if (Objects.isNull(msg)) {
      return null;
    }
    String message = messageSource.getMessage(msg, new Object[] {}, NOT_FOUND_MESSAGE, LocaleContextHolder.getLocale());
    if (NOT_FOUND_MESSAGE.equals(message)) {
      return msg;
    }
    return message;
  }
}
