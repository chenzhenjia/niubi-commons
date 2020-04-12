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
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author chenzhenjia
 * @since 2020/4/12
 */
public abstract class AbstractResponseMsgFormatter implements ResponseMessageCodeFormatter {
    private final MessageSource messageSource;

    protected AbstractResponseMsgFormatter(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMsg(String msg) {
        if (support(msg)) {
            String code = getCode(msg);
            return messageSource.getMessage(code, new Object[]{}, null, LocaleContextHolder.getLocale());
        }
        return msg;
    }

    protected abstract boolean support(String msg);

    protected abstract String getCode(String msg);
}
