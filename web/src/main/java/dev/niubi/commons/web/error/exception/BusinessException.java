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

package dev.niubi.commons.web.error.exception;

import org.springframework.http.HttpStatus;

import dev.niubi.commons.web.json.Response;

/**
 * @author chenzhenjia
 * @since 2020/1/29
 */
public class BusinessException extends RuntimeException {
    private String code = Response.Codes.BUSINESS;
    private HttpStatus status;

    public BusinessException() {
        super("未知错误");
    }

    public BusinessException(Response<?> response) {
        this(response.getMsg(), response.getCode(), HttpStatus.valueOf(response.getStatus()));
    }

    public BusinessException(String message, String code) {
        this(message, code, null);
    }

    public BusinessException(String message, String code,
                             HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public BusinessException(String message,
                             HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BusinessException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
