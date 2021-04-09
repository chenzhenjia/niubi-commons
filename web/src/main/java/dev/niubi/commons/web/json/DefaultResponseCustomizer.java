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

import dev.niubi.commons.web.json.i18n.ResponseMessageCodeFormatter;
import java.util.Date;
import java.util.HashMap;

/**
 * @author chenzhenjia
 * @since 2021/04/19
 */
public class DefaultResponseCustomizer implements ResponseCustomizer {

  private final ResponseMessageCodeFormatter responseMessageCodeFormatter;

  public DefaultResponseCustomizer(
      ResponseMessageCodeFormatter responseMessageCodeFormatter) {
    this.responseMessageCodeFormatter = responseMessageCodeFormatter;
  }

  public Object customize(Response<?> response) {
    HashMap<String, Object> map = response.toMap();
    map.put("timestamp", new Date());
    map.put("msg", responseMessageCodeFormatter.getMsg(response.getMsg()));
    return map;
  }
}
