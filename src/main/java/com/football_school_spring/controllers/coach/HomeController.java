package com.football_school_spring.controllers.coach;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.services.TeamCreationService;
import com.football_school_spring.utils.SecurityContextHolderAuthenticationSetter;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class HomeController extends CoachController {
    private static final Logger logger = getLogger(HomeController.class);

    @Autowired
    private TeamCreationService teamCreationService;
    @Autowired
    private CoachRepository coachRepository;

    @GetMapping("/home")
    public String home(Model model) {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (coach.getTeamCoaches().isEmpty()) {
            if (coach.getMaxNumberOfTeams() == 0) {
                return "no-team-possible";
            }
            return "coach-init-team";
        }
        return "coach-home";
    }

    @PostMapping("/init-team")
    public String initTeam(@RequestParam Map<String, String> requestParams, Model model, WebRequest request) {
        Team newTeam = new Team(requestParams.remove("name"), requestParams.remove("address"));
        List<String> coachesMails = requestParams.values().stream()
                .filter(coachMail -> !coachMail.isEmpty())
                .collect(Collectors.toList());

        try {
            teamCreationService.create(newTeam, coachesMails, request);
            logger.info("Team correctly created");

            // unnecessary to update number of teams of currently logged user
            updateSecurityContextHolder();
            return UrlCleaner.redirectWithCleaning(model, "/coach/home");
        } catch (Exception e) {
            logger.error("Problems during team creation", e);
            return UrlCleaner.redirectWithCleaning(model, "/coach/home?error=true");
        }
    }

    private void updateSecurityContextHolder() {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolderAuthenticationSetter.set(coachRepository.findById(coach.getId())
                .orElseThrow(() -> new IllegalArgumentException("Can't get coach from DB"))
        );
    }
}