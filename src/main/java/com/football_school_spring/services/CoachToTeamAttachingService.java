package com.football_school_spring.services;

import com.football_school_spring.models.Team;

public interface CoachToTeamAttachingService {
    void attach(Team team, String coachMail);
}