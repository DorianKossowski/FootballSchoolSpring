package com.football_school_spring.services;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.enums.CoachPrivilegeName;

public interface CoachCreationService {
    void createCoach(Coach coach, CoachPrivilegeName coachPrivilegeName);
}