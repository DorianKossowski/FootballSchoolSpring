package com.football_school_spring.controllers.basic_user.coach;

import com.football_school_spring.controllers.basic_user.PossibleTeamsController;
import com.football_school_spring.models.Coach;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coach")
class CoachController extends PossibleTeamsController {
    boolean isManager(Coach coach, long teamId) {
        return coach.getTeamCoaches().stream()
                .anyMatch(teamCoach -> teamCoach.getTeam().getId() == teamId &&
                        teamCoach.getCoachPrivilege().getName().equals(CoachPrivilegeName.MANAGER.getName())
                );
    }
}