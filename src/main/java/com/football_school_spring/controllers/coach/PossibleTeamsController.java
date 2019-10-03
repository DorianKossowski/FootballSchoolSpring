package com.football_school_spring.controllers.coach;

import com.football_school_spring.controllers.AuthorizedUserController;
import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.TeamCoach;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.stream.Collectors;

public class PossibleTeamsController extends AuthorizedUserController {

    @ModelAttribute("teams")
    public Map<Long, String> getCoachTeams() {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<Long, String> teams = coach.getTeamCoaches().stream()
                .map(TeamCoach::getTeam)
                .collect(Collectors.toMap(Team::getId, Team::getName));
        if (coach.getTeamCoaches().size() < coach.getMaxNumberOfTeams()) {
            teams.put(-1L, "Create new team");
        }
        return teams;
    }

    @ModelAttribute("currentTeam")
    public String getCurrentTeam(HttpSession session) {
        return String.valueOf(session.getAttribute("currentTeam"));
    }
}