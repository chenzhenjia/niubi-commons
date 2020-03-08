package dev.niubi.commons.web.error;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
