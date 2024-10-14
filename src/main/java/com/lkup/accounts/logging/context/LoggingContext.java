
package com.lkup.accounts.logging.context;

import java.util.Map;

public class LoggingContext {
    private static final ThreadLocal<LoggingContext> contextHolder = new ThreadLocal<>();

    private String requestId;
    private String requestUrl;
    private String method;
    private Map<String, String> requestHeaders;
    private Map<String, String[]> requestParams;
    private Map<String, String> pathVariables;

    public static LoggingContext getCurrentContext() {
        return contextHolder.get();
    }

    public static void setCurrentContext(LoggingContext context) {
        contextHolder.set(context);
    }

    public static void clear() {
        contextHolder.remove();
    }

    // Getters and setters for all fields
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, String[]> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, String[]> requestParams) {
        this.requestParams = requestParams;
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }

    public void setPathVariables(Map<String, String> pathVariables) {
        this.pathVariables = pathVariables;
    }
}
    