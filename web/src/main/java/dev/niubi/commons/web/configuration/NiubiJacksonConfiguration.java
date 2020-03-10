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

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */
@Configuration
@ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
public class NiubiJacksonConfiguration {

    @Bean
    public NiubiJacksonBuilderCustomizer niubiJacksonBuilderCustomizer() {
        return new NiubiJacksonBuilderCustomizer();
    }

    /**
     * 让 Jackson 支持 jdk8 和 javaTime
     */
    static class NiubiJacksonBuilderCustomizer implements Ordered, Jackson2ObjectMapperBuilderCustomizer {
        public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
            jacksonObjectMapperBuilder.modulesToInstall(new Jdk8Module(),
                    new JavaTimeModule());
        }

        public int getOrder() {
            return 1;
        }
    }

}
