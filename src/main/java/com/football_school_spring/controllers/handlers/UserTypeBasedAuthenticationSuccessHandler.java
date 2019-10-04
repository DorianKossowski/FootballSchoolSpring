package com.football_school_spring.controllers.handlers;

import com.football_school_spring.controllers.coach.PossibleTeamsController;
import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.models.enums.UserTypeName;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserTypeBasedAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final String ADMIN_URL = "/admin/coaches-list";
    private static final String COACH_URL = "/coach/home";
    private static final String PARENT_URL = "/parent/home";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (isTypeOf(authentication, UserTypeName.ADMIN)) {
            this.getRedirectStrategy().sendRedirect(request, response, ADMIN_URL);
            return;
        }
        if (isTypeOf(authentication, UserTypeName.COACH)) {
            setInitCurrentTeam(request.getSession(), (Coach) authentication.getPrincipal());
            this.getRedirectStrategy().sendRedirect(request, response, COACH_URL);
            return;
        }
        if (isTypeOf(authentication, UserTypeName.PARENT)) {
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

    private void setInitCurrentTeam(HttpSession session, Coach coach) {
        if (!coach.getTeamCoaches().isEmpty()) {
            Team currentTeamInDb = coach.getTeamCoaches().iterator().next().getTeam();
            session.setAttribute(PossibleTeamsController.CURRENT_TEAM,
                    new CurrentTeamDTO(currentTeamInDb.getId(), currentTeamInDb.getName()));
        } else {
            session.setAttribute(PossibleTeamsController.CURRENT_TEAM, new CurrentTeamDTO());
        }
    }
}