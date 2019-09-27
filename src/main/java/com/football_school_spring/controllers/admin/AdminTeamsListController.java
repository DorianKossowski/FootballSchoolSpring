package com.football_school_spring.controllers.admin;

import com.football_school_spring.models.DTO.TeamsListDTO;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminTeamsListController extends AdminController {
    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/teams-list")
    public String showCoachesList(Model model) {
        List<Team> teams = teamRepository.findAll();

        List<TeamsListDTO> teamsListDTOS = getTeamsListDTOS(teams);
        model.addAttribute("teamsListDTOS", teamsListDTOS);
        model.addAttribute("teamsSize", teamsListDTOS.size());
        return "admin-teams-list";
    }

    private List<TeamsListDTO> getTeamsListDTOS(List<Team> teams) {
        List<TeamsListDTO> teamsListDTOS = new ArrayList<>();
        teams.forEach(team -> {
            long id = team.getId();
            String name = team.getName();
            String managerName = team.getTeamCoaches().stream()
                    .filter(teamCoach -> teamCoach.getCoachPrivilege().getName().equals(CoachPrivilegeName.MANAGER.getName()))
                    .map(teamCoach -> teamCoach.getCoach().getName() + " " + teamCoach.getCoach().getSurname())
                    .collect(Collectors.toList()).get(0);
            teamsListDTOS.add(new TeamsListDTO(id, name, managerName));
        });
        return teamsListDTOS;
    }
}