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
