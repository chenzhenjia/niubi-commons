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

import java.util.Map;

/**
 * 自定义错误处理
 *
 * @author chenzhenjia
 * @since 2019/12/11
 */
public interface ResponseErrorCustomizer {

  /**
   * 用来自定义错误处理的方法
   *
   * @param errorAttributes Spring mvc 的所有错误信息
   * @return 返回到前端的值
   */
  Map<String, Object> customize(Map<String, Object> errorAttributes);
}
