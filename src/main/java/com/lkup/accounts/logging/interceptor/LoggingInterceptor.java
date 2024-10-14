
package com.lkup.accounts.logging.interceptor;


import com.lkup.accounts.logging.context.LoggingContext;
import com.lkup.accounts.logging.util.RequestExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (LoggingContext.getCurrentContext() == null) {
            LoggingContext context = new LoggingContext();
            context.setRequestId(UUID.randomUUID().toString());
            context.setRequestUrl(request.getRequestURI());
            context.setMethod(request.getMethod());

            context.setRequestHeaders(RequestExtractor.extractHeaders(request));
            context.setRequestParams(RequestExtractor.extractRequestParams(request));
            context.setPathVariables(RequestExtractor.extractPathVariables(request));

            LoggingContext.setCurrentContext(context);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LoggingContext.clear(); // Clear context after completion
    }
}
    