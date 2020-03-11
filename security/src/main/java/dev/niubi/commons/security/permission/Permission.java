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

package dev.niubi.commons.security.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 *
 * @author chenzhenjia
 * @since 2019/11/21
 * <pre>
 * &#64;RestController
 * &#64;Permission(tag = "添加管理员", value = {"admin:user"})
 * &#64;RequestMapping("user")
 * public class UserCtrl {
 *
 *  &#64;PostMapping
 *  &#64;Permission(tag = "添加管理员", value = {"admin:user:post"})
 *  public Response<?> add(@RequestBody @Validated AdminUserIn addIn) {
 *      return Response.ok();
 *  }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
public @interface Permission {
    /**
     * 权限的 key
     */
    String[] value();

    /**
     * 权限的 tag 名字
     */
    String tag();
}
