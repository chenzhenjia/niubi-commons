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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import dev.niubi.commons.web.json.i18n.ResponseMessageCodeFormatter;
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
    public static class DefaultCode {
        /**
         * 成功
         */
        public static final String SUCCESS = "ok";
        /**
         * 业务异常
         */
        public static final String BUSINESS = "business";
        /**
         * 未知异常
         */
        public static final String UNKNOWN = "unknown";
        public static final String NOT_FOUND = "notFound";
        public static final String DELETE_FAILURE = "deleteFailure";
    }

    private static ResponseMessageCodeFormatter messageCodeFormatter;
    private static volatile ObjectMapper objectMapper = new ObjectMapper();

    public static synchronized void setObjectMapper(ObjectMapper newObjectMapper) {
        Objects.requireNonNull(newObjectMapper, "ObjectMapper 不能为空");
        objectMapper = newObjectMapper;
    }

    protected static void setMessageCodeFormatter(ResponseMessageCodeFormatter messageCodeFormatter) {
        Response.messageCodeFormatter = messageCodeFormatter;
    }

    /**
     * 状态码
     */
    private HttpStatus status;
    @Getter
    private String code;
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

    @JsonCreator
    public Response(@JsonProperty("code") String code,
                    @JsonProperty("status") Integer status,
                    @JsonProperty("body") T body, @JsonProperty("msg") String msg,
                    @JsonProperty("extra") Map<String, Object> extra) {
        this.status = Optional.ofNullable(status).map(HttpStatus::valueOf).orElse(null);
        this.msg = msg;
        this.body = body;
        this.code = code;
        this.extra = extra;
    }

    public Response(String code, HttpStatus status, T body, String msg,
                    Map<String, Object> extra) {
        this.status = status;
        this.msg = msg;
        this.body = body;
        this.code = code;
        this.extra = extra;
    }

    public int getStatus() {
        return getHttpStatus().value();
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return Optional.ofNullable(status).orElse(HttpStatus.OK);
    }

    public boolean isSuccess() {
        return DefaultCode.SUCCESS.equals(code);
    }

    @NotNull
    public final HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.msg != null) {
            map.put("msg", this.getMsg());
        }
        if (this.extra != null) {
            map.put("extra", this.extra);
        }
        Date timestamp = this.timestamp;
        if (timestamp == null) {
            timestamp = new Date();
        }
        map.put("status", getStatus());
        map.put("code", this.code);
        map.put("body", this.body);
        map.put("timestamp", timestamp);
        map.put("success", this.isSuccess());
        return map;
    }

    public Response<T> writeHeader(HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(getStatus());
        return this;
    }

    public void write(HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), writeHeader(response).toMap());
    }

    public void writeAndFlash(HttpServletResponse response) throws IOException {
        write(response);
        response.flushBuffer();
    }

    public void write(ObjectMapper objectMapper, HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), writeHeader(response).toMap());
    }

    public void writeAndFlash(ObjectMapper objectMapper, HttpServletResponse response) throws IOException {
        write(objectMapper, response);
        response.flushBuffer();
    }

    public static <T> Response<T> ok(T body) {
        return ok().body(body);
    }

    public static Builder ok(String msg) {
        return new Builder(DefaultCode.SUCCESS).status(HttpStatus.OK).msg(msg);
    }

    public static Builder ok() {
        return ok(messageCodeFormatter.defaultMsg().getOk());
    }

    public static Builder business(String msg) {
        return status(DefaultCode.BUSINESS).msg(msg);
    }

    public static <T> Response<T> business(String msg, T body) {
        return business(msg).body(body);
    }

    public static Builder status(String code) {
        return new Builder(code);
    }

    public static Builder deleteFailure() {
        return deleteFailure(messageCodeFormatter.defaultMsg().getDeleteFailure());
    }

    public static Builder deleteFailure(String msg) {
        return new Builder(DefaultCode.DELETE_FAILURE).msg(msg);
    }

    public static <T> Response<T> deleteFailure(T body) {
        return deleteFailure().body(body);
    }

    public static <T> Response<T> notfound(T body) {
        return notfound().body(body);
    }

    public static Builder notfound() {
        return notfound(messageCodeFormatter.defaultMsg().getNotFound());
    }

    public static Builder notfound(String msg) {
        return status(DefaultCode.NOT_FOUND).msg(msg).status(HttpStatus.NOT_FOUND);
    }

    public static <T> Response<T> unknown(T body) {
        return unknown().body(body);
    }

    public static Builder unknown() {
        return unknown(messageCodeFormatter.defaultMsg().getUnknown());
    }

    public static Builder unknown(String msg) {
        return status(DefaultCode.UNKNOWN).msg(msg)
          .status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static class Builder {
        /**
         * 相应码
         */
        private final String code;
        /**
         * 消息
         */
        private String msg;
        /**
         * 返回的额外的数据
         */
        private Map<String, Object> extra;
        private HttpStatus status;

        public Builder(String code) {
            this.code = code;
        }

        public Builder msg(String msg) {
            this.msg = messageCodeFormatter.getMsg(msg);
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

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public <T> Response<T> build() {
            return body(null);
        }

        public <T> Response<T> body(T body) {
            return new Response<>(Optional.ofNullable(code).orElse(DefaultCode.UNKNOWN), status, body, msg, extra);
        }
    }

}
