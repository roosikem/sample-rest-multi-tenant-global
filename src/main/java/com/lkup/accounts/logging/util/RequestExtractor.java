
package com.lkup.accounts.logging.util;

import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestExtractor {

    public static Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> enumHeaders = request.getHeaderNames();
        while (enumHeaders.hasMoreElements()) {
            String headerName = enumHeaders.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    public static Map<String, String[]> extractRequestParams(HttpServletRequest request) {
        return request.getParameterMap();
    }

    public static Map<String, String> extractPathVariables(HttpServletRequest request) {
        return (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }
}
    