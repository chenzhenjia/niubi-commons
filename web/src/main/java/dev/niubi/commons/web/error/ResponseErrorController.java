package dev.niubi.commons.web.error;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenzhenjia
 * @since 2019/11/21
 */
public class ResponseErrorController extends BasicErrorController {
    private final ResponseErrorCustomizer responseErrorCustomizer;

    public ResponseErrorController(ErrorAttributes errorAttributes,
                                   ErrorProperties errorProperties,
                                   List<ErrorViewResolver> errorViewResolvers,
                                   ResponseErrorCustomizer responseErrorCustomizer) {
        super(errorAttributes, errorProperties, errorViewResolvers);
        this.responseErrorCustomizer = responseErrorCustomizer;
    }

    @Override
    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
        return responseErrorCustomizer.customize(errorAttributes);
    }
}
