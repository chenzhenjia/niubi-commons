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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author chenzhenjia
 * @since 2019/12/14
 */
public class ExceptionsHandlerRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        boolean exceptionHandle = exceptionHandle(metadata);
        if (exceptionHandle) {
            String beanName = ExceptionsHandler.class.getName();
            if (!registry.containsBeanDefinition(beanName)) {
                GenericBeanDefinition definition = new GenericBeanDefinition();
                definition.setBeanClass(ExceptionsHandler.class);
                definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
                registry.registerBeanDefinition(beanName, definition);
            }
        }
    }

    private boolean exceptionHandle(AnnotationMetadata metadata) {
        return metadata.getAnnotations().stream(EnableResponseError.class)
                .map((annotation) -> annotation.getBoolean("exceptionsHandler"))
                .reduce(true, (a, a1) -> a && a1);
    }
}
