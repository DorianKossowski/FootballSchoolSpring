package com.football_school_spring.services;

import com.football_school_spring.models.Team;

import java.util.Map;

public interface TeamManageService {
    void updateTeam(Team updatedTeam);

    void deleteCoachFromTeam(String coachId, long teamId);

    void assignNewCoaches(Map<String, String> requestParams, long teamId);
}