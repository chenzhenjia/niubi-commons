/*
 * Copyright 2021 陈圳佳
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author chenzhenjia
 * @since 2020/4/12
 */
public class DefaultMessageCodeFormatter implements ResponseMessageCodeFormatter {

  private final MessageSource messageSource;
  private static final Pattern REGX = Pattern.compile("(\\{[a-zA-Z._]+})");
  private static final Pattern KEY_REGX = Pattern.compile("^\\{|}$");

  public DefaultMessageCodeFormatter(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public String getMsg(String msg) {
    if (Objects.isNull(msg)) {
      return null;
    }
    String replacedMsg = msg;
    Matcher matcher = REGX.matcher(msg);
    while (matcher.find()) {
      String key = matcher.group();
      key = KEY_REGX.matcher(key).replaceAll("");
      int start = matcher.start();
      int end = matcher.end();
      String message = messageSource
          .getMessage(key, new Object[] {}, null, LocaleContextHolder.getLocale());
      if (Objects.isNull(message)) {
        continue;
      }
      replacedMsg = replacedMsg.replace(msg.substring(start, end), message);
    }
    return replacedMsg;
  }
}
