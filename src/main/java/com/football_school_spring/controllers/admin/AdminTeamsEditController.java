package com.football_school_spring.controllers.admin;

import com.football_school_spring.models.Team;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.ObjectsDeletingService;
import com.football_school_spring.utils.UrlCleaner;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class AdminTeamsEditController extends AdminController {
    private static final Logger logger = getLogger(AdminTeamsEditController.class);

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ObjectsDeletingService objectsDeletingService;

    @GetMapping("/team/{id}")
    public String showTeam(Model model, @PathVariable("id") String teamId) {
        Team team = teamRepository.findById(Long.valueOf(teamId))
                .orElseThrow(() -> new GettingFromDbException(Team.class, Long.parseLong(teamId)));
        model.addAttribute("team", team);
        model.addAttribute("manager", team.getManager());
        model.addAttribute("numberOfPlayers", playerRepository.findByTeamId(Long.valueOf(teamId)).size());
        model.addAttribute("teamCoaches", team.getTeamCoaches());
        return "admin-team";
    }

    @PostMapping("/teams-edit/delete/{teamId}")
    public String deleteTeam(Model model, @PathVariable("teamId") String teamId) {
        try {
            objectsDeletingService.deleteTeam(Long.parseLong(teamId));
            logger.info("Team correctly deleted");
            return UrlCleaner.redirectWithCleaning(model, "/admin/teams-list");
        } catch (Exception e) {
            logger.error("Error during deleting team", e);
            return UrlCleaner.redirectWithCleaning(model, String.format("/admin/team/%s?error=true", teamId));
        }
    }
}