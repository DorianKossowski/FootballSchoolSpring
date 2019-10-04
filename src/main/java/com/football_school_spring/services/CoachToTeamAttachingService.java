package com.football_school_spring.services;

import com.football_school_spring.models.Team;
import org.springframework.web.context.request.WebRequest;

public interface CoachToTeamAttachingService {
    void attach(WebRequest request, Team team, String coachMail);
}