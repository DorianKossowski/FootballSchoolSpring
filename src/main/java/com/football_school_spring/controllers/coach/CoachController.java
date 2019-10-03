package com.football_school_spring.controllers.coach;

import com.football_school_spring.models.Team;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coach")
class CoachController extends PossibleTeamsController {
    Team currentTeam;
}