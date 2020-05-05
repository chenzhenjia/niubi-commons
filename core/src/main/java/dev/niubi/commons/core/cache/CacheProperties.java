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
@ConfigurationProperties(prefix = "niubi.cache")
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
