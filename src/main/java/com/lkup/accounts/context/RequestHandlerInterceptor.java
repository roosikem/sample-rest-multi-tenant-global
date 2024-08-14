package com.lkup.accounts.context;

import com.lkup.accounts.exceptions.BadRequestException;
import com.lkup.accounts.exceptions.InvalidTeamIdException;
import com.lkup.accounts.exceptions.InvalidTenantIdException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;
import java.util.UUID;

@Component
@Order(2)
public class RequestHandlerInterceptor implements HandlerInterceptor {
    private static final String TENANT_ID = "**";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if("/api/v1/auth/authenticate".equalsIgnoreCase(uri)) {
            return true;
        }
        String teamId = request.getHeader("X-TeamId");
        String tenantId = request.getHeader("X-TenantId");
        if(Objects.isNull(tenantId))
            throw new InvalidTenantIdException("tenantId is missing");
        if(Objects.isNull(teamId)) {
            throw new InvalidTeamIdException("Team Id is missing");
        }
        RequestInfo requestInfo = new RequestInfo(UUID.randomUUID().toString().replace("-", ""), teamId, tenantId);
        RequestContext.setRequestContext(requestInfo);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContext.clearRequestContext();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
