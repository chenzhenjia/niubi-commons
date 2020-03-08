package dev.niubi.commons.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

/**
 * 自定义的缓存设置,现在仅支持 Redis
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
@Configuration
@EnableConfigurationProperties(value = CacheProperties.class)
@ConditionalOnClass(RedisOperations.class)
public class CacheConfiguration {
    private final CacheProperties cacheProperties;

    @Autowired
    public CacheConfiguration(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer niuBiRedisCacheManagerBuilderCustomizer() {
        return builder -> {
            CacheProperties.Redis defaultConfig = cacheProperties.getRedis()
                    .get("default");
            if (Objects.nonNull(defaultConfig)) {
                builder.cacheDefaults(redisCacheConfiguration(defaultConfig));
            }

            Map<String, RedisCacheConfiguration> configs = cacheProperties.getRedis()
                    .entrySet()
                    .stream()
                    .filter(en -> !en.getKey().equals("default"))
                    .collect(Collectors.toMap(Map.Entry::getKey, en -> redisCacheConfiguration(en.getValue())));
            builder.withInitialCacheConfigurations(configs);
        };
    }

    protected RedisCacheConfiguration redisCacheConfiguration(CacheProperties.Redis redisProperties) {
        RedisCacheConfiguration config = defaultCacheConfig();
        RedisCacheConfiguration finalConfig = config;
        config = Optional.ofNullable(redisProperties.getKeyPrefix())
                .map(it -> finalConfig.computePrefixWith(cacheName -> it + "::" + cacheName + "::"))
                .orElse(config)
        ;
        config = Optional.ofNullable(redisProperties.getTimeToLive())
                .map(config::entryTtl)
                .orElse(config)
        ;
        config = Optional.ofNullable(redisProperties.getTimeToLive())
                .map(config::entryTtl)
                .orElse(config)
        ;
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        // if (!redisProperties.isUseKeyPrefix()) {
        //     config = config.disableKeyPrefix();
        // }
        return config;
    }

}
