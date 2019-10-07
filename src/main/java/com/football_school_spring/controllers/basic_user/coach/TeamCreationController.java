package com.football_school_spring.controllers.basic_user.coach;

import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.services.TeamCreationService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class TeamCreationController extends CoachController {
    private static final Logger logger = getLogger(TeamCreationController.class);

    @Autowired
    private TeamCreationService teamCreationService;

    @GetMapping("/create-team")
    public String getCreateTeam() {
        return "coach-create-team";
    }

    @PostMapping("/create-team")
    public String postCreateTeam(@RequestParam Map<String, String> requestParams, Model model, HttpSession session) {
        Team newTeam = new Team(requestParams.remove("name"), requestParams.remove("address"));
        List<String> coachesMails = requestParams.values().stream()
                .filter(coachMail -> !coachMail.isEmpty())
                .collect(Collectors.toList());

        try {
            teamCreationService.create(newTeam, coachesMails);
            // unnecessary to update number of teams of currently logged user
            updateSecurityContextHolder();
            logger.info("Team correctly created");

            session.setAttribute(CURRENT_TEAM, new CurrentTeamDTO(newTeam));
            return UrlCleaner.redirectWithCleaning(model, "/coach/home");
        } catch (Exception e) {
            logger.error("Problems during team creation", e);
            return UrlCleaner.redirectWithCleaning(model, "/coach/home?error=true");
        }
    }
}