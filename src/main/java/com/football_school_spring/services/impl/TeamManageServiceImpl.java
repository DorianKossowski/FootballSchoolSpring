package com.football_school_spring.services.impl;

import com.football_school_spring.models.Team;
import com.football_school_spring.repositories.TeamCoachRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.CoachToTeamAttachingService;
import com.football_school_spring.services.TeamManageService;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void deleteCoachFromTeam(long coachId, long teamId) {
        Team teamInDB = teamRepository.findById(teamId)
                .orElseThrow(() -> new GettingFromDbException(Team.class, teamId));

        teamInDB.getTeamCoaches().stream()
                .filter(teamCoach -> teamCoach.getCoach().getId() == coachId)
                .findFirst().ifPresent(teamCoach -> teamCoachRepository.delete(teamCoach));
    }

    @Override
    public void assignNewCoaches(List<String> coachesMails, long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GettingFromDbException(Team.class, teamId));
        coachesMails.forEach(coachMail -> {
            if (!coachMail.isEmpty())
                coachToTeamAttachingService.attach(team, coachMail);
        });
    }
}