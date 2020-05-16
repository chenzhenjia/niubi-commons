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

package dev.niubi.commons.web.error;

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.niubi.commons.web.json.Response;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author chenzhenjia
 * @since 2020/3/24
 */
@ControllerAdvice
@Slf4j
public class KotlinExceptionHandler {
    @ExceptionHandler(MissingKotlinParameterException.class)
    public ResponseEntity<Response<?>> handleMissingKotlinParameter(MissingKotlinParameterException e) {
        Response<Object> response = Response.business(e.getMsg()).code(BAD_REQUEST.value()).msg(e.getMsg()).build();
        return ResponseEntity.badRequest().body(response);
    }
}
