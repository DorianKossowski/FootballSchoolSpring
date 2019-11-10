package com.football_school_spring.controllers.handlers;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Parent;
import com.football_school_spring.models.enums.UserTypeName;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.services.VerificationTokenService;
import com.football_school_spring.utils.CurrentTeamInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserTypeBasedAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final String ADMIN_URL = "/admin/coaches-list";
    private static final String COACH_URL = "/coach/home";
    private static final String PARENT_URL = "/parent/home";

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (isTypeOf(authentication, UserTypeName.ADMIN)) {
            verificationTokenService.cleanUpUsersWithExpiredTokens();
            CurrentTeamInitializer.setDefaultInitCurrentTeam(request.getSession());
            this.getRedirectStrategy().sendRedirect(request, response, ADMIN_URL);
            return;
        }
        if (isTypeOf(authentication, UserTypeName.COACH)) {
            CurrentTeamInitializer.setInitCurrentTeam(request.getSession(), (Coach) authentication.getPrincipal());
            this.getRedirectStrategy().sendRedirect(request, response, COACH_URL);
            return;
        }
        if (isTypeOf(authentication, UserTypeName.PARENT)) {
            CurrentTeamInitializer.setInitCurrentTeam(request.getSession(), (Parent) authentication.getPrincipal(), playerRepository);
            this.getRedirectStrategy().sendRedirect(request, response, PARENT_URL);
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private boolean isTypeOf(Authentication authentication, UserTypeName typeName) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals(typeName.getName()));
    }
}