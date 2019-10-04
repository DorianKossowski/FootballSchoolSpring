package com.football_school_spring.controllers.coach;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.utils.GettingFromDbException;
import com.football_school_spring.utils.SecurityContextHolderAuthenticationSetter;
import com.football_school_spring.utils.UrlCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;

@Controller
public class TeamManageController extends CoachController {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/manage")
    public String getManage(Model model, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        if (!isManager(currentTeamDTO.getId())) {
            return UrlCleaner.redirectWithCleaning(model, "/coach/home?notManager");
        }
        Team team = teamRepository.findById(currentTeamDTO.getId()).orElseThrow(() -> new GettingFromDbException(Team.class));
        model.addAttribute("team", team);
        return "coach-manage";
    }

    private boolean isManager(long teamId) {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return coach.getTeamCoaches().stream()
                .anyMatch(teamCoach -> teamCoach.getTeam().getId() == teamId &&
                        teamCoach.getCoachPrivilege().getName().equals(CoachPrivilegeName.MANAGER.getName())
                );
    }

    @PostMapping("/manage")
    public String postManage(Model model, @ModelAttribute Team team, HttpSession session) {
        Team teamInDB = teamRepository.findById(team.getId())
                .orElseThrow(() -> new GettingFromDbException(Team.class, team.getId()));
        teamInDB.setName(team.getName());
        teamInDB.setAddress(team.getAddress());
        teamRepository.save(teamInDB);
        session.setAttribute(CURRENT_TEAM, new CurrentTeamDTO(teamInDB.getId(), teamInDB.getName()));


        // unnecessary to update panel with teams of currently logged coach
        long loggedUserId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        SecurityContextHolderAuthenticationSetter.set(userRepository.findById(loggedUserId)
                .orElseThrow(() -> new GettingFromDbException(User.class)));

        return UrlCleaner.redirectWithCleaning(model, "/coach/manage");
    }
}