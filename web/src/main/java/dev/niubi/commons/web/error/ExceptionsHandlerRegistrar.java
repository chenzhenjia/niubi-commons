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
