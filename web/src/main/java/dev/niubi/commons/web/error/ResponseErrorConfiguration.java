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

package dev.niubi.commons.web.error;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.List;
import java.util.stream.Collectors;

import dev.niubi.commons.web.error.support.DefaultResponseErrorCustomizer;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */
@Configuration
@ConditionalOnWebApplication
public class ResponseErrorConfiguration {
    private final ServerProperties serverProperties;
    private final List<ErrorViewResolver> errorViewResolvers;

    @Autowired
    public ResponseErrorConfiguration(ServerProperties serverProperties,
                                      ObjectProvider<ErrorViewResolver> errorViewResolvers) {
        this.serverProperties = serverProperties;
        this.errorViewResolvers = errorViewResolvers.stream().collect(Collectors.toList());
    }

    @Bean
    public ResponseErrorController responseErrorController(ErrorAttributes errorAttributes,
                                                           ResponseErrorCustomizer responseErrorCustomizer) {
        return new ResponseErrorController(errorAttributes, this.serverProperties.getError(),
                this.errorViewResolvers, responseErrorCustomizer);
    }

    @Bean
    @ConditionalOnMissingBean(ResponseErrorCustomizer.class)
    public DefaultResponseErrorCustomizer defaultResponseErrorCustomizer() {
        return new DefaultResponseErrorCustomizer();
    }

    @Configuration
    @Order
    @ConditionalOnClass(WebSecurityConfigurerAdapter.class)
    public static class ResponseErrorSecurityConfiguration extends WebSecurityConfigurerAdapter {
        private final ServerProperties serverProperties;

        ResponseErrorSecurityConfiguration(ServerProperties serverProperties) {
            this.serverProperties = serverProperties;
        }

        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(serverProperties.getError().getPath());
        }
    }
}
