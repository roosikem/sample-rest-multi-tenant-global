package com.lkup.accounts.context;

import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.document.User;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.exceptions.BadRequestException;
import com.lkup.accounts.exceptions.InvalidTeamIdException;
import com.lkup.accounts.exceptions.InvalidTenantIdException;
import com.lkup.accounts.service.OrganizationService;
import com.lkup.accounts.service.TeamService;
import com.lkup.accounts.utilities.PermissionConstants;
import com.lkup.accounts.utilities.RoleChecker;
import com.lkup.accounts.utilities.RoleConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(2)
public class RequestHandlerInterceptor implements HandlerInterceptor {
    private final OrganizationService organizationService;
    private final RoleChecker roleChecker;
    private final TeamService teamService;

    RequestHandlerInterceptor(RoleChecker roleChecker, OrganizationService organizationService, TeamService teamService) {
        this.organizationService = organizationService;
        this.roleChecker = roleChecker;
        this.teamService = teamService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String teamId = request.getHeader("X-TeamId");
        String tenantId = request.getHeader("X-TenantId");
        if(!roleChecker.hasSuperAdminRole()) {
            if(Objects.isNull(tenantId))
                throw new InvalidTenantIdException("tenantId is missing");
            if(Objects.isNull(teamId)) {
                throw new InvalidTeamIdException("Team Id is missing");
            }

            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(!principal.getOrganization().getId().equalsIgnoreCase(tenantId)) {
                throw new InvalidTenantIdException("User is not authorized for this action.");
            }

            Optional<Organization> organization = organizationService.findOrganizationById(tenantId);
            if(!organization.isPresent())
                throw new InvalidTenantIdException("invalid tenant id");

            if(!roleChecker.hasUserTeamAccess(teamId)) {
                throw new InvalidTenantIdException("User is not authorized for this action.");
            }
            Optional<Team> team = teamService.findTeamById(teamId);
            if(!team.isPresent())
                throw new InvalidTenantIdException("invalid team id");
        }

        RequestInfo requestInfo = null;
        requestInfo = new RequestInfo(UUID.randomUUID().toString().replace("-", ""), teamId, tenantId);
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
