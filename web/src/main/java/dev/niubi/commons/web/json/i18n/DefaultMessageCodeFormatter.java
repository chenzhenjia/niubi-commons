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

import org.springframework.context.MessageSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenzhenjia
 * @since 2020/4/12
 */
public class DefaultMessageCodeFormatter extends AbstractResponseMsgFormatter {
    public static final Pattern REG = Pattern.compile("\\{(?<code>.+)}");

    public DefaultMessageCodeFormatter(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    protected String getCode(String msg) {
        Matcher matcher = REG.matcher(msg);
        if (matcher.find()) {
            return matcher.group("code");
        }
        return null;
    }
}
