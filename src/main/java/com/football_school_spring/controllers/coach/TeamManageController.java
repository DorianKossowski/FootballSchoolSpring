package com.football_school_spring.controllers.coach;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.TeamCoach;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.TeamManageService;
import com.football_school_spring.utils.GettingFromDbException;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class TeamManageController extends CoachController {
    private static final Logger logger = getLogger(TeamManageController.class);

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamManageService teamManageService;

    @GetMapping("/manage")
    public String getManage(Model model, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!isManager(coach, currentTeamDTO.getId())) {
            return UrlCleaner.redirectWithCleaning(model, "/coach/home?notManager");
        }
        Team team = teamRepository.findById(currentTeamDTO.getId()).orElseThrow(() -> new GettingFromDbException(Team.class));
        model.addAttribute("team", team);
        model.addAttribute("coaches", getTeamCoaches(team));
        return "coach-manage";
    }

    @PostMapping("/manage")
    public String postManage(Model model, @ModelAttribute Team team, HttpSession session) {
        try {
            teamManageService.updateTeam(team);
            // unnecessary to update panel with teams of currently logged coach
            updateSecurityContextHolder();
            logger.info("Team correctly updated");

            session.setAttribute(CURRENT_TEAM, new CurrentTeamDTO(team.getId(), team.getName()));
            return UrlCleaner.redirectWithCleaning(model, "/coach/manage?updated=true");
        } catch (Exception e) {
            logger.error("Error during updating team", e);
            return UrlCleaner.redirectWithCleaning(model, "/coach/manage?error=true");
        }
    }

    @PostMapping("/delete-coach/{coachId}")
    public String deleteCoach(Model model, @PathVariable("coachId") String coachId, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        try {
            teamManageService.deleteCoachFromTeam(coachId, currentTeamDTO.getId());
            logger.info(String.format("User with id %d is not %s coach anymore", Long.parseLong(coachId), currentTeamDTO.getName()));

            return UrlCleaner.redirectWithCleaning(model, "/coach/manage?updated=true");
        } catch (Exception e) {
            logger.error("Error during updating team", e);
            return UrlCleaner.redirectWithCleaning(model, "/coach/manage?error=true");
        }
    }

    @PostMapping("/manage-coaches")
    public String assignCoaches(@RequestParam Map<String, String> requestParams, Model model, WebRequest request,
                                @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        try {
            teamManageService.assignNewCoaches(requestParams, request, currentTeamDTO.getId());
            logger.info("Added new staff to team");

            return UrlCleaner.redirectWithCleaning(model, "/coach/manage?updated=true");
        } catch (Exception e) {
            logger.error("Error during updating team", e);
            return UrlCleaner.redirectWithCleaning(model, "/coach/manage?error=true");
        }
    }

    private List<Coach> getTeamCoaches(Team team) {
        return team.getTeamCoaches().stream()
                .filter(teamCoach -> teamCoach.getCoachPrivilege().getName().equals(CoachPrivilegeName.COACH.getName()))
                .map(TeamCoach::getCoach)
                .collect(Collectors.toList());
    }

    private boolean isManager(Coach coach, long teamId) {
        return coach.getTeamCoaches().stream()
                .anyMatch(teamCoach -> teamCoach.getTeam().getId() == teamId &&
                        teamCoach.getCoachPrivilege().getName().equals(CoachPrivilegeName.MANAGER.getName())
                );
    }
}