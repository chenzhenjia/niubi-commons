package dev.niubi.commons.web.json;

/**
 * @author chenzhenjia
 * @since 2019/12/11
 */
public interface ResponseCustomizer {

    Object customize(Response<?> response);

}
