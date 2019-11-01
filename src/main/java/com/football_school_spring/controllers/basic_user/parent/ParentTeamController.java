package com.football_school_spring.controllers.basic_user.parent;

import com.football_school_spring.models.Parent;
import com.football_school_spring.models.Player;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
public class ParentTeamController extends ParentController {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/team")
    public String getTeam(Model model, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        Parent parent = (Parent) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Team team = teamRepository.findById(currentTeamDTO.getId())
                .orElseThrow(() -> new GettingFromDbException(Team.class, currentTeamDTO.getId()));
        List<Player> playersByParentAndTeam = playerRepository.findByParentIdAndTeamId(parent.getId(), currentTeamDTO.getId());

        model.addAttribute("team", team);
        model.addAttribute("manager", team.getManager());
        model.addAttribute("players", playersByParentAndTeam);
        model.addAttribute("numberOfPlayers", playerRepository.findByTeamId(currentTeamDTO.getId()).size());
        model.addAttribute("teamCoaches", team.getTeamCoaches());
        return "parent-team";
    }
}