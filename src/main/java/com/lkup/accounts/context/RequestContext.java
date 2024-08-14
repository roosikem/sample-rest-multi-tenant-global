package com.lkup.accounts.context;

public class RequestContext {

    private static final ThreadLocal<RequestInfo> reqContext = new InheritableThreadLocal<>();

    public static RequestInfo getRequestContext() {
        return reqContext.get();
    }

    public static void setRequestContext(RequestInfo requestInfo) {
        reqContext.set(requestInfo);
    }

    public static void clearRequestContext() {
        reqContext.remove();
    }
}
