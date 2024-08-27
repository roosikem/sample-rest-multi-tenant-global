package com.lkup.accounts.utilities;

import com.lkup.accounts.context.RequestContext;
import com.lkup.accounts.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class OrganizationTeamValidator {
    private final RoleChecker roleChecker;

    public OrganizationTeamValidator(RoleChecker roleChecker) {
        this.roleChecker = roleChecker;
    }

    public boolean validateOrganizationTeam(String organizationId, String teamId) {
        String requestTenantId = null;
        String requestTeamId = null;
        if (!roleChecker.hasSuperAdminRole()) {
            requestTenantId = RequestContext.getRequestContext().getTenantId();
            requestTeamId = RequestContext.getRequestContext().getTeamId();

            if (!requestTeamId.equals(teamId)) {
                throw new BadRequestException("Invalid Team ID. Team ID should match the team ID in request");
            }
            if (!requestTenantId.equals(organizationId)) {
                throw new BadRequestException("Invalid Tenant ID. Tenant ID should match the tenant ID in request");
            }
        } else {
            requestTenantId = organizationId;
            requestTeamId = teamId;
        }

        if (!roleChecker.hasUserAccessTeamAndOrganization(requestTenantId, requestTeamId)) {
            throw new BadRequestException("User not authorized to perform this action");
        }
        return true;
    }

    public boolean isSuperAdmin() {
        return roleChecker.hasSuperAdminRole();
    }
}
