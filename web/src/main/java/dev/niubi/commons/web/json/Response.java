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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 公用返回对象
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
@Data
public class Response<T> {
    public static class Status {
        /**
         * 成功
         */
        public static final String SUCCESS = "ok";
        static String SUCCESS_MSG = "";
        /**
         * 业务异常
         */
        public static final String BUSINESS = "business";
        /**
         * 未知异常
         */
        public static final String UNKNOWN = "unknown";
        static String UNKNOWN_MSG = "";

        public static final String NOT_FOUND = "notFound";

        static String NOT_FOUND_MSG = "";

        public static final String DELETE_FAILURE = "deleteFailure";

        static String DELETE_FAILURE_MSG = "";
    }

    /**
     * 状态码
     */
    @Getter
    private final String status;
    private Integer code;
    /**
     * 消息
     */
    @Getter
    private String msg;
    /**
     * 内容
     */
    private T body;
    /**
     * 时间
     */
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private Long timestamp;
    /**
     * 返回的额外的数据
     */
    private Map<String, Object> extra;

    @JsonCreator
    public Response(@JsonProperty("status") String status, @JsonProperty("code") Integer code,
                    @JsonProperty("body") T body, @JsonProperty("msg") String msg,
                    @JsonProperty("extra") Map<String, Object> extra) {
        this.status = status;
        this.msg = msg;
        this.body = body;
        this.code = code;
        this.extra = extra;
    }


    public boolean isSuccess() {
        return Status.SUCCESS.equals(status);
    }

    @NotNull
    public final HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.msg != null) {
            map.put("msg", this.msg);
        }
        if (this.extra != null) {
            map.put("extra", this.extra);
        }
        Long timestamp = this.timestamp;
        if (timestamp == null) {
            timestamp = System.currentTimeMillis();
        }
        map.put("status", this.status);
        map.put("code", this.code);
        map.put("body", this.body);
        map.put("timestamp", timestamp);
        map.put("success", this.isSuccess());
        return map;
    }

    public static <T> Response<T> ok(T body) {
        return ok().body(body);
    }

    public static Builder ok(String msg) {
        return ok().msg(msg);
    }

    public static Builder ok(Integer code) {
        return ok().code(code);
    }

    public static Builder ok() {
        return new Builder(Status.SUCCESS).msg(Status.SUCCESS_MSG);
    }

    public static Builder deleteFailure() {
        return new Builder(Status.DELETE_FAILURE).msg(Status.DELETE_FAILURE_MSG);
    }

    public static Builder deleteFailure(String msg) {
        return deleteFailure().msg(msg);
    }

    public static Builder deleteFailure(Integer code) {
        return deleteFailure().code(code);
    }

    public static <T> Response<T> deleteFailure(T body) {
        return deleteFailure().body(body);
    }

    private static Builder business() {
        return status(Status.BUSINESS);
    }

    public static Builder business(String msg) {
        return business().msg(msg);
    }

    private static Builder status(String status) {
        return new Builder(status);
    }

    public static <T> Response<T> notfound(T body) {
        return notfound().body(body);
    }

    public static Builder notfound(String msg) {
        return notfound().msg(msg);
    }

    public static Builder notfound(Integer code) {
        return notfound().code(code);
    }

    public static Builder notfound() {
        return status(Status.NOT_FOUND).msg(Status.NOT_FOUND_MSG);
    }

    public static Builder unknown() {
        return status(Status.UNKNOWN).msg(Status.UNKNOWN_MSG);
    }

    public static Builder unknown(String msg) {
        return unknown().msg(msg);
    }

    public static class Builder {
        /**
         * 状态码
         */
        private final String status;
        private Integer code;
        /**
         * 消息
         */
        private String msg;
        /**
         * 返回的额外的数据
         */
        private Map<String, Object> extra;

        public Builder(String status) {
            this.status = status;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }


        public Builder code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder extra(String key, Object value) {
            if (this.extra == null) {
                this.extra = new HashMap<>();
            }
            extra.put(key, value);
            return this;
        }

        public Builder extra(Map<String, ?> extra) {
            if (this.extra == null) {
                this.extra = new HashMap<>();
            }
            this.extra.putAll(extra);
            return this;
        }

        public <T> Response<T> build() {
            return body(null);
        }

        public <T> Response<T> body(T body) {
            return new Response<>(Optional.ofNullable(status).orElse(Status.UNKNOWN), Optional.ofNullable(code).orElse(0), body, msg, extra);
        }
    }

}
