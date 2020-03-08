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
