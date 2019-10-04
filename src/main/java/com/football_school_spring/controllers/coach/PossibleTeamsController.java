package com.football_school_spring.controllers.coach;

import com.football_school_spring.controllers.AuthorizedUserController;
import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.TeamCoach;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Map;
import java.util.stream.Collectors;

public class PossibleTeamsController extends AuthorizedUserController {
    private static final String TEAMS = "teams";
    public static final String CURRENT_TEAM = "currentTeam";

    @ModelAttribute(TEAMS)
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

    @ModelAttribute(CURRENT_TEAM)
    public String getCurrentTeam(@SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        return currentTeamDTO.getName();
    }
}