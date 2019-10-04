package com.football_school_spring.services.impl;

import com.football_school_spring.models.Team;
import com.football_school_spring.repositories.TeamCoachRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.CoachToTeamAttachingService;
import com.football_school_spring.services.TeamManageService;
import com.football_school_spring.utils.GettingFromDbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeamManageServiceImpl implements TeamManageService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamCoachRepository teamCoachRepository;
    @Autowired
    private CoachToTeamAttachingService coachToTeamAttachingService;

    @Override
    public void updateTeam(Team updatedTeam) {
        Team teamInDB = teamRepository.findById(updatedTeam.getId())
                .orElseThrow(() -> new GettingFromDbException(Team.class, updatedTeam.getId()));
        teamInDB.setName(updatedTeam.getName());
        teamInDB.setAddress(updatedTeam.getAddress());
        teamRepository.save(teamInDB);
    }

    @Override
    public void deleteCoachFromTeam(String coachId, long teamId) {
        Team teamInDB = teamRepository.findById(teamId)
                .orElseThrow(() -> new GettingFromDbException(Team.class, teamId));

        teamInDB.getTeamCoaches().stream()
                .filter(teamCoach -> teamCoach.getCoach().getId() == Long.parseLong(coachId))
                .findFirst().ifPresent(teamCoach -> teamCoachRepository.delete(teamCoach));
    }

    @Override
    public void assignNewCoaches(Map<String, String> requestParams, WebRequest request, long teamId) {
        List<String> coachesMails = requestParams.values().stream()
                .filter(coachMail -> !coachMail.isEmpty())
                .collect(Collectors.toList());
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new GettingFromDbException(Team.class, teamId));
        coachesMails.forEach(coachMail -> coachToTeamAttachingService.attach(request, team, coachMail));
    }
}