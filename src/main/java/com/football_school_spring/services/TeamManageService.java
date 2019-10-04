package com.football_school_spring.services;

import com.football_school_spring.models.Team;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

public interface TeamManageService {
    void updateTeam(Team updatedTeam);

    void deleteCoachFromTeam(String coachId, long teamId);

    void assignNewCoaches(Map<String, String> requestParams, WebRequest request, long teamId);
}