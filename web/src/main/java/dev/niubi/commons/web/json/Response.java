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

import java.util.Date;
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
    private Date timestamp;
    /**
     * 返回的额外的数据
     */
    private Map<String, Object> extra;
    public static final Response<Object> UNKNOWN = new Response<>(Status.UNKNOWN, null, Status.UNKNOWN_MSG, null);

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

    private Response(String status, T body, String msg, Map<String, Object> extra) {
        this(status, 0, body, msg, extra);
    }

    public boolean isSuccess() {
        return Status.SUCCESS.equals(status);
    }

    public Response<T> putExtra(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>();
        }
        extra.put(key, value);
        return this;
    }

    public Response<T> putAllExtra(Map<String, ?> extra) {
        if (this.extra == null) {
            this.extra = new HashMap<>();
        }
        this.extra.putAll(extra);
        return this;
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
        map.put("status", this.status);
        map.put("code", this.code);
        map.put("body", this.body);
        map.put("timestamp", this.timestamp);
        map.put("success", this.isSuccess());
        return map;
    }

    public static <T> Response<T> ok(T body, String msg) {
        return status(Status.SUCCESS, msg, body);
    }

    public static <T> Response<T> ok(Integer status, T body, String msg) {
        return status(Status.SUCCESS + Optional.ofNullable(status).orElse(0), msg, body);
    }

    public static <T> Response<T> ok(T body) {
        return ok(body, Status.SUCCESS_MSG);
    }

    public static <T> Response<T> ok() {
        return ok(null);
    }

    public static <T> Response<T> deleteFailure() {
        return deleteFailure(null);
    }

    public static <T> Response<T> deleteFailure(String msg) {
        return deleteFailure(msg, null);
    }

    public static <T> Response<T> deleteFailure(String msg, T body) {
        return status(Status.DELETE_FAILURE, msg, body);
    }

    public static <T> Response<T> deleteFailure(T body) {
        return deleteFailure(Status.DELETE_FAILURE_MSG, body);
    }

    public static <T> Response<T> business(Integer status, String msg, T body) {
        return status(Status.BUSINESS, Optional.ofNullable(status).orElse(0), msg, body);
    }

    public static <T> Response<T> business(Integer status, String msg) {
        return business(status, msg, null);
    }

    public static <T> Response<T> business(String msg, T body) {
        return business(0, msg, body);
    }

    public static <T> Response<T> business(String msg) {
        return business(msg, null);
    }

    private static <T> Response<T> status(String status, Integer code, String msg, T body) {
        return new Response<>(status, code, body, msg, null);
    }

    private static <T> Response<T> status(String status, Integer code, String msg) {
        return status(status, code, msg, null);
    }

    private static <T> Response<T> status(String status, String msg) {
        return status(status, 0, msg, null);
    }

    private static <T> Response<T> status(String status, String msg, T body) {
        return status(status, 0, msg, body);
    }

    public static <T> Response<T> notfound(String msg, T body) {
        return status(Status.NOT_FOUND, msg, body);
    }

    public static <T> Response<T> notfound(String msg) {
        return notfound(msg, null);
    }

    public static <T> Response<T> notfound() {
        return notfound(Status.DELETE_FAILURE_MSG);
    }

    @SuppressWarnings("unchecked")
    public static <T> Response<T> unknown() {
        return ((Response<T>) UNKNOWN);
    }

    public static <T> Response<T> unknown(String msg) {
        return status(Status.UNKNOWN, msg);
    }

}
