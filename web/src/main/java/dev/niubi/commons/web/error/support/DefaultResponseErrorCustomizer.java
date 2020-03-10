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

package dev.niubi.commons.web.error.support;

import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

import dev.niubi.commons.web.error.ResponseErrorCustomizer;
import dev.niubi.commons.web.json.Response;

/**
 * 默认的错误消息返回实现
 *
 * @author chenzhenjia
 * @since 2019/12/11
 */
public class DefaultResponseErrorCustomizer implements ResponseErrorCustomizer {
    @Override
    public Map<String, Object> customize(Map<String, Object> errorAttributes) {
        Integer statusCode = MapUtils.getInteger(errorAttributes, "status");
        String error = MapUtils.getString(errorAttributes, "error");
        String message = MapUtils.getString(errorAttributes, "message");
        String path = MapUtils.getString(errorAttributes, "path");
        HashMap<String, Object> map = Response.business(statusCode, message).toMap();
        map.put("path", path);
        map.put("error", error);
        return map;
    }
}
