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

package dev.niubi.commons.web.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class NiubiWebMvcConfiguration {
    /**
     * 让 Spring Data 的 Pageable 的分页从第一页开始 <br/> {@link org.springframework.data.domain.Pageable}
     */
    @Bean
    @ConditionalOnClass(PageableHandlerMethodArgumentResolverCustomizer.class)
    public PageableHandlerMethodArgumentResolverCustomizer pageableResolverCustomizer() {
        return pageableResolver -> pageableResolver.setOneIndexedParameters(true);
    }
}
