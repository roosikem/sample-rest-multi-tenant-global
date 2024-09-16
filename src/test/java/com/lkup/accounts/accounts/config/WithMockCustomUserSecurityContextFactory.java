package com.lkup.accounts.accounts.config;

import com.lkup.accounts.document.Role;
import com.lkup.accounts.document.User;
import com.lkup.accounts.utilities.PermissionConstants;
import com.lkup.accounts.utilities.RoleConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        String username = customUser.username();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Map<String, User> userMap = (Map<String, User>) TestDataInitializer.getDataObjectbyName(TestDataInitializer.USERS_MAP);
        User principal = userMap.get(username);
        if(Objects.isNull(principal)) {
            principal =
                    new User();
            Role role = new Role();
            role.setId("1");
            role.setName(RoleConstants.SUPER_ADMIN_ROLE);
            principal.setRole(role);
            principal.setUsername(customUser.username());
            List<String> permissions = new ArrayList<>();
            permissions.add(PermissionConstants.ADMINISTRATOR);
            Authentication auth =
                    UsernamePasswordAuthenticationToken.authenticated(principal, "password", permissions
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
            context.setAuthentication(auth);
        } else {
            Authentication auth =
                    UsernamePasswordAuthenticationToken.authenticated(principal, "password", principal.getAuthorities());
            context.setAuthentication(auth);
        }


        return context;
    }
}
