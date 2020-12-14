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

package dev.niubi.commons.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author chenzhenjia
 * @since 2020/12/14
 */
class SpringMvcResponse<T> extends Response<T> {
    /**
     * 时间
     */
    private Date timestamp;
    /**
     * 是否启用了i18n
     */
    @JsonIgnore
    private final boolean i18n;
    private final HttpStatus status;

    public SpringMvcResponse(String code, HttpStatus status, T body,
                             String msg, Map<String, Object> extra, boolean i18n) {
        super(code, status.value(), body, msg, extra);
        this.i18n = i18n;
        this.status =status;
    }

    public boolean isI18n() {
        return i18n;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return Optional.ofNullable(status).orElse(HttpStatus.OK);
    }

    @NotNull
    @Override
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = super.toMap();
        HashMap<String, Object> newMap = new HashMap<>(map);
        Date timestamp = this.timestamp;
        if (timestamp == null) {
            timestamp = new Date();
        }
        map.put("timestamp", timestamp);
        return newMap;
    }
}
