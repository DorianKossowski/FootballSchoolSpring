package com.football_school_spring.controllers.basic_user;

import com.football_school_spring.controllers.AuthorizedUserController;
import com.football_school_spring.models.*;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class PossibleTeamsController extends AuthorizedUserController {
    private static final String TEAMS = "teams";
    public static final String CURRENT_TEAM = "currentTeam";
    public static final String CURRENT_TEAM_ID = "currentTeamId";

    @Autowired
    private PlayerRepository playerRepository;

    @ModelAttribute(TEAMS)
    public Map<Long, String> getCoachTeams() {
        Map<Long, String> teams;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof Coach) {
            Coach coach = (Coach) user;
            teams = coach.getTeamCoaches().stream()
                    .map(TeamCoach::getTeam)
                    .collect(Collectors.toMap(Team::getId, Team::getName));
            if (coach.getNumberOfTeamsAsManager() < coach.getMaxNumberOfTeams()) {
                teams.put(-1L, "Create new team");
            }
        } else if (user instanceof Parent) {
            Parent parent = (Parent) user;
            teams = playerRepository.findByParentId(parent.getId()).stream()
                    .map(Player::getTeam)
                    .distinct()
                    .collect(Collectors.toMap(Team::getId, Team::getName));
        } else {
            // Admin case
            return Collections.emptyMap();
        }
        return teams;
    }

    @ModelAttribute(CURRENT_TEAM)
    public String getCurrentTeam(@SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        return currentTeamDTO.getName();
    }

    @ModelAttribute(CURRENT_TEAM_ID)
    public long getCurrentTeamId(@SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        return currentTeamDTO.getId();
    }
}