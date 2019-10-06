package com.football_school_spring.controllers.coach;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class HomeController extends CoachController {
    private static final Logger logger = getLogger(HomeController.class);

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (coach.getTeamCoaches().isEmpty()) {
            if (coach.getMaxNumberOfTeams() == 0) {
                return "no-team-possible";
            }
            return "coach-init-team";
        }

        return "coach-home";
    }

    @GetMapping("/set-team/{teamId}")
    public String setCurrentTeam(Model model, HttpSession session, @PathVariable("teamId") String teamId) {
        Optional<Team> teamOptional = teamRepository.findById(Long.valueOf(teamId));
        teamOptional.ifPresent(team -> session.setAttribute(CURRENT_TEAM, new CurrentTeamDTO(team)));
        logger.info(String.format("%s is new team in session", session.getAttribute(CURRENT_TEAM)));
        return UrlCleaner.redirectWithCleaning(model, "/coach/home");
    }
}