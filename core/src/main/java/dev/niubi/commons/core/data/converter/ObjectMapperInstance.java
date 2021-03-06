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

package dev.niubi.commons.core.data.converter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import java.util.Collection;

/**
 * @author chenzhenjia
 * @since 2020/5/16
 */
class ObjectMapperInstance {

  static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
      .activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
              .allowIfSubType(Collection.class).build(), ObjectMapper.DefaultTyping.NON_FINAL,
          JsonTypeInfo.As.WRAPPER_OBJECT)
      .build();
}
