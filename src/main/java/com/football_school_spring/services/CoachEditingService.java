package com.football_school_spring.services;

import com.football_school_spring.models.Coach;
import com.football_school_spring.utils.CoachEditingValidationResult;

public interface CoachEditingService {
    CoachEditingValidationResult isEditingValid(long coachId);

    CoachEditingValidationResult isTeamsNumberEditingValid(Coach coach, int newNumberOfTeams);

    void changeCoachStatus(Coach coach);

    void setMaxNumberOfTeams(Coach coach, int newNumberOfTeams);
}