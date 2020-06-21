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

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
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

    @SuppressWarnings("deprecation")
    @Override
    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
        return responseErrorCustomizer.customize(errorAttributes);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                     ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        return responseErrorCustomizer.customize(errorAttributes);
    }
}
