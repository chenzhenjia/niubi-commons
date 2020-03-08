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
