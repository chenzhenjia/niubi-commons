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
