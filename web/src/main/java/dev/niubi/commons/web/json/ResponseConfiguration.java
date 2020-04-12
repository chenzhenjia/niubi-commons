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

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ResourceBundleMessageSource;

import dev.niubi.commons.web.json.i18n.DefaultMessageCodeFormatter;
import dev.niubi.commons.web.json.i18n.DefaultMsg;
import dev.niubi.commons.web.json.i18n.ResponseMessageCodeFormatter;

/**
 * @author chenzhenjia
 * @since 2020/3/25
 */
@Configuration
public class ResponseConfiguration {

    @Bean
    @Lazy(false)
    public ResponseAdvice responseAdvice(ObjectProvider<ResponseCustomizer> responseCustomizers,
                                         ResponseMessageCodeFormatter messageCodeFormatter) {

        return new ResponseAdvice(responseCustomizers.getIfAvailable(() -> response -> response), messageCodeFormatter);
    }

    protected MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("niubi_messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(true);
        return messageSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public ResponseMessageCodeFormatter responseMessageCodeFormatter(ObjectProvider<MessageSource> messageSource) {

        DelegatingMessageSource delegatingMessageSource = new DelegatingMessageSource(messageSource());
        delegatingMessageSource.setPrimaryMessageSource(messageSource.getIfAvailable());

        return new DefaultMessageCodeFormatter(delegatingMessageSource);
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> responseContextInitFinishListener(ResponseMessageCodeFormatter messageCodeFormatter) {
        return event -> {
            DefaultMsg defaultMsg = messageCodeFormatter.defaultMsg();
            Response.Status.DELETE_FAILURE_MSG = defaultMsg.getDeleteFailure();
            Response.Status.NOT_FOUND_MSG = defaultMsg.getNotFound();
            Response.Status.SUCCESS_MSG = defaultMsg.getOk();
            Response.Status.UNKNOWN_MSG = defaultMsg.getUnknown();
        };
    }

}
