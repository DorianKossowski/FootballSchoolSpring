package com.football_school_spring.controllers.basic_user.parent;

import com.football_school_spring.models.Parent;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.repositories.PlayerRepository;
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
public class ParentHomeController extends ParentController {
    private static final Logger logger = getLogger(ParentHomeController.class);

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private FixturesService fixturesService;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/home")
    public String home(Model model, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        Parent parent = (Parent) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (playerRepository.findByParentId(parent.getId()).isEmpty()) {
            return "parent-no-player";
        }
        fixturesService.getNextFixture(currentTeamDTO.getId())
                .ifPresent(fixture -> model.addAttribute("fixture", fixture));
        model.addAttribute("messages", chatMessageService.getMessagesDTO(currentTeamDTO.getId()));
        return "parent-home";
    }

    @GetMapping("/set-team/{teamId}")
    public String setCurrentTeam(Model model, HttpSession session, @PathVariable("teamId") String teamId) {
        Optional<Team> teamOptional = teamRepository.findById(Long.valueOf(teamId));
        teamOptional.ifPresent(team -> session.setAttribute(CURRENT_TEAM, new CurrentTeamDTO(team)));
        logger.info(String.format("%s is new team in session", session.getAttribute(CURRENT_TEAM)));
        return UrlCleaner.redirectWithCleaning(model, "/parent/home");
    }
}