package com.football_school_spring.services;

import com.football_school_spring.models.Team;

import java.util.List;

public interface TeamManageService {
    void updateTeam(Team updatedTeam);

    void deleteCoachFromTeam(long coachId, long teamId);

    void assignNewCoaches(List<String> coachesMails, long teamId);
}