package dev.niubi.commons.core.exception;

/**
 * @author chenzhenjia
 * @since 2020/1/29
 */
public class BusinessException extends RuntimeException {
    private Integer code = 0;

    public BusinessException() {
        super("未知错误");
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
