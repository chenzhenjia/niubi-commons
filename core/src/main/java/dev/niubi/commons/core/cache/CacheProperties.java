package dev.niubi.commons.core.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

import lombok.Data;

/**
 * 缓存配置属性
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
@ConfigurationProperties(prefix = "dev.niubi.cache")
@Data
public class CacheProperties {
    /**
     * Redis 的配置
     */
    private Map<String, Redis> redis;

    @Data
    public static class Redis {
        /**
         * 过期时间
         */
        private Duration timeToLive;
        /**
         * RedisCacheManager 的缓存前缀
         */
        private String keyPrefix;
        /**
         * 是否缓存 null 的值
         */
        private boolean cacheNullValues;
    }
}
