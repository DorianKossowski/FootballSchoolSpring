package com.football_school_spring.utils;

import com.football_school_spring.controllers.basic_user.PossibleTeamsController;
import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Parent;
import com.football_school_spring.models.Player;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.repositories.PlayerRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

public class CurrentTeamInitializer {

    public static void setInitCurrentTeam(HttpSession session, Coach coach) {
        if (!coach.getTeamCoaches().isEmpty()) {
            Team currentTeamInDb = coach.getTeamCoaches().iterator().next().getTeam();
            session.setAttribute(PossibleTeamsController.CURRENT_TEAM, new CurrentTeamDTO(currentTeamInDb));
        } else {
            session.setAttribute(PossibleTeamsController.CURRENT_TEAM, new CurrentTeamDTO());
        }
    }

    public static void setInitCurrentTeam(HttpSession session, Parent parent, PlayerRepository playerRepository) {
        List<Player> playersByParent = playerRepository.findByParentId(parent.getId());
        if (!playersByParent.isEmpty()) {
            Team currentTeamInDb = playersByParent.iterator().next().getTeam();
            session.setAttribute(PossibleTeamsController.CURRENT_TEAM, new CurrentTeamDTO(currentTeamInDb));
        } else {
            session.setAttribute(PossibleTeamsController.CURRENT_TEAM, new CurrentTeamDTO());
        }
    }
}