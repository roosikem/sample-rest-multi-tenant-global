package com.lkup.accounts.utilities;

import com.lkup.accounts.document.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleChecker {

    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    public boolean hasAnyRole(List<String> roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return roles.stream().anyMatch(authorities::contains);
    }

    public boolean hasSuperAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        User user = (User) authentication.getPrincipal();
        return user.getRole().getName().equals(RoleConstants.SUPER_ADMIN_ROLE);
    }

    public boolean hasUserTeamAccess(String teamId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        User user = (User) authentication.getPrincipal();
        return user.getTeams().stream()
                .anyMatch(team -> team.getId().equals(teamId));
    }

    public boolean hasUserAccessTeamAndOrganization(String requestTenantId, String requestedTeamId) {
        if (hasSuperAdminRole()) {
            return true;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        User user = (User) authentication.getPrincipal();
        boolean isTeamAccess = user.getTeams().stream()
                .anyMatch(team -> team.getId().equals(requestedTeamId));
        boolean isOrganizationAccess = user.getOrganization().getId().equals(requestTenantId);

        return isTeamAccess && isOrganizationAccess;

    }
}
