package com.football_school_spring.controllers.basic_user.coach;

import com.football_school_spring.controllers.basic_user.PossibleTeamsController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coach")
class CoachController extends PossibleTeamsController {
}