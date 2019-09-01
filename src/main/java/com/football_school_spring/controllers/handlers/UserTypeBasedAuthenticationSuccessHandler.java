package com.football_school_spring.controllers.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserTypeBasedAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private String adminRoleTargetUrl;
    private String adminRoleAuthority;

    public UserTypeBasedAuthenticationSuccessHandler(String defaultTargetUrl, String adminRoleTargetUrl, String adminRoleAuthority) {
        super(defaultTargetUrl);
        this.adminRoleTargetUrl = adminRoleTargetUrl;
        this.adminRoleAuthority = adminRoleAuthority;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (isAdmin(authentication)) {
            this.getRedirectStrategy().sendRedirect(request, response, this.getAdminRoleTargetUrl());
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals(getAdminRoleAuthority()));
    }

    private String getAdminRoleTargetUrl() {
        return adminRoleTargetUrl;
    }

    private String getAdminRoleAuthority() {
        return adminRoleAuthority;
    }
}