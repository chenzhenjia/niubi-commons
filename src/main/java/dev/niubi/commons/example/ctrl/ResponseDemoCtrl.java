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

package dev.niubi.commons.example.ctrl;

import dev.niubi.commons.web.json.Response;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenzhenjia
 * @since 2021/1/27
 */
@RestController
@RequestMapping("demo")
public class ResponseDemoCtrl {

  @GetMapping
  public Response<Object> get() {
    return Response.ok().build();
  }

  @GetMapping("i18n")
  public Response<Object> i18n() {
    return Response.ok("{say}{hello.word}").build();
  }

  @GetMapping("entity")
  public ResponseEntity<Response<Object>> entity() {
    return ResponseEntity.badRequest().body(Response.ok("测试").build());
  }

  @GetMapping("write")
  public void write(HttpServletResponse response) throws IOException {
    Response.ok().build().write(response);
  }
}
