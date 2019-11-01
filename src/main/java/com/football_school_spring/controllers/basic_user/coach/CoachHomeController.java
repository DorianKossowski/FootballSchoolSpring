package com.football_school_spring.controllers.basic_user.coach;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.ChatMessageService;
import com.football_school_spring.services.FixturesService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class CoachHomeController extends CoachController {
    private static final Logger logger = getLogger(CoachHomeController.class);

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private FixturesService fixturesService;
    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/home")
    public String home(Model model, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (coach.getTeamCoaches().isEmpty()) {
            if (coach.getMaxNumberOfTeams() == 0) {
                return "coach-no-team-possible";
            }
            return "coach-init-team";
        }
        fixturesService.getNextFixture(currentTeamDTO.getId())
                .ifPresent(fixture -> model.addAttribute("fixture", fixture));
        model.addAttribute("messages", chatMessageService.getMessagesDTO(currentTeamDTO.getId(), coach));
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