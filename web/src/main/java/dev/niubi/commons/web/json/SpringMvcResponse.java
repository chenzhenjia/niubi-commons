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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;

/**
 * @author chenzhenjia
 * @since 2020/12/14
 */
public class SpringMvcResponse<T> extends Response<T> {

  private final HttpStatus status;
  private final Date timestamp;

  public SpringMvcResponse(String code, HttpStatus status, T body,
      String msg, Map<String, Object> extra) {
    super(code, status.value(), body, msg, extra);
    this.status = status;
    this.timestamp = new Date();
  }

  public Date getTimestamp() {
    return timestamp;
  }

  @JsonIgnore
  public HttpStatus getHttpStatus() {
    return Optional.ofNullable(status).orElse(HttpStatus.OK);
  }
}
