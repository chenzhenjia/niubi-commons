/*
 * Copyright 2021 陈圳佳
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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * 公用返回对象
 *
 * @author chenzhenjia
 * @since 2019/11/21
 */
public class Response<T> {

  private static volatile ObjectMapper objectMapper;
  private static volatile ResponseCustomizer responseCustomizer;
  /**
   * 状态码
   */
  private final int status;
  private final String code;
  /**
   * 内容
   */
  private final T body;
  /**
   * 返回的额外的数据
   */
  private final Map<String, Object> extra;
  /**
   * 消息
   */
  private String msg;

  @JsonCreator
  public Response(@JsonProperty("code") String code,
      @JsonProperty("status") Integer status,
      @JsonProperty("body") T body,
      @JsonProperty("msg") String msg,
      @JsonProperty("extra") Map<String, Object> extra) {
    Objects.requireNonNull(code, "code 不能为空");
    this.status = Optional.ofNullable(status).orElse(200);
    this.msg = msg;
    this.body = body;
    this.code = code;
    this.extra = extra;
  }

  private synchronized static ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
    }
    return objectMapper;
  }
 private synchronized static ResponseCustomizer getResponseCustomizer() {
    if (responseCustomizer == null) {
      responseCustomizer = ResponseCustomizer.DEFAULT;
    }
    return responseCustomizer;
  }

  static synchronized void setObjectMapper(ObjectMapper newObjectMapper) {
    Objects.requireNonNull(newObjectMapper, "ObjectMapper 不能为空");
    objectMapper = newObjectMapper;
  }

  static synchronized void setResponseCustomizer(ResponseCustomizer responseCustomizer) {
    Objects.requireNonNull(responseCustomizer, "responseCustomizer 不能为空");
    Response.responseCustomizer = responseCustomizer;
  }

  public static <T> Response<T> ok(T body) {
    return ok().body(body);
  }

  public static Builder ok(String msg) {
    return new Builder(Codes.SUCCESS)
        .status(200).msg(msg);
  }

  public static Builder ok() {
    return ok(MsgCodes.SUCCESS);
  }

  public static Builder business(String msg) {
    return code(Codes.BUSINESS).msg(msg);
  }

  public static <T> Response<T> business(String msg, T body) {
    return business(msg).body(body);
  }

  public static Builder code(String code) {
    return new Builder(code);
  }

  public static Builder deleteFailure() {
    return deleteFailure(MsgCodes.DELETE_FAILURE);
  }

  public static Builder deleteFailure(String msg) {
    return new Builder(Codes.DELETE_FAILURE).msg(msg);
  }

  public static <T> Response<T> deleteFailure(T body) {
    return deleteFailure().body(body);
  }

  public static <T> Response<T> notfound(T body) {
    return notfound().body(body);
  }

  public static Builder notfound() {
    return notfound(MsgCodes.NOT_FOUND);
  }

  public static Builder notfound(String msg) {
    return code(Codes.NOT_FOUND).msg(msg).status(HttpStatus.NOT_FOUND);
  }

  public static <T> Response<T> unknown(T body) {
    return unknown().body(body);
  }

  public static Builder unknown() {
    return unknown(MsgCodes.UNKNOWN);
  }

  public static Builder copy(Response<?> response) {
    return new Builder(response.code).with(response);
  }

  public static Builder unknown(String msg) {
    return code(Codes.UNKNOWN).msg(msg)
        .status(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public int getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getBody() {
    return body;
  }

  public Map<String, Object> getExtra() {
    return extra;
  }

  public boolean isSuccess() {
    return Codes.SUCCESS.equals(code);
  }

  @Override
  public String toString() {
    return "Response{" +
        "success=" + isSuccess() +
        ", status=" + getStatus() +
        ", code='" + code + '\'' +
        ", msg='" + msg + '\'' +
        '}';
  }

  @NotNull
  public HashMap<String, Object> toMap() {
    HashMap<String, Object> map = new HashMap<>();
    if (this.msg != null) {
      map.put("msg", this.getMsg());
    }
    if (this.extra != null) {
      map.put("extra", this.extra);
    }
    map.put("status", getStatus());
    map.put("code", this.code);
    map.put("body", this.body);
    map.put("success", this.isSuccess());
    return map;
  }

  public Response<T> writeHeader(HttpServletResponse response) {
    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(getStatus());
    return this;
  }

  public void write(HttpServletResponse response) throws IOException {
    write(getObjectMapper(), response);
  }

  public void writeAndFlash(HttpServletResponse response) throws IOException {
    write(response);
    response.flushBuffer();
  }

  public void write(ObjectMapper objectMapper, HttpServletResponse response) throws IOException {
    Object value = getResponseCustomizer().customize(writeHeader(response));
    objectMapper.writeValue(response.getWriter(), value);
  }

  public void writeAndFlash(ObjectMapper objectMapper, HttpServletResponse response) throws IOException {
    write(objectMapper, response);
    response.flushBuffer();
  }

  public static class Codes {

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

  public static class MsgCodes {

    public static final String SUCCESS = "{Response.success}";
    public static final String UNKNOWN = "{Response.unknown}";
    public static final String NOT_FOUND = "{Response.notfound}";
    public static final String DELETE_FAILURE = "{Response.deleteFailure}";
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
      this.msg = msg;
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
      if (extra == null) {
        return this;
      }
      if (this.extra == null) {
        this.extra = new HashMap<>();
      }
      this.extra.putAll(extra);
      return this;
    }

    public Builder status(int status) {
      this.status = HttpStatus.resolve(status);
      return this;
    }

    public Builder status(HttpStatus status) {
      this.status = status;
      return this;
    }

    public Builder with(Response<?> response) {
      return msg(response.msg)
          .status(response.getStatus())
          .extra(response.extra)
          ;
    }

    public <T> Response<T> build() {
      return body(null);
    }

    public <T> Response<T> body(T body) {
      String code = Optional.ofNullable(this.code).orElse(Codes.UNKNOWN);
      HttpStatus status = Optional.ofNullable(this.status).orElse(HttpStatus.OK);
      return new SpringMvcResponse<>(code,
          status, body, msg, extra);
    }
  }
}
