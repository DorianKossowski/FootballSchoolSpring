package com.football_school_spring.controllers.coach;

import com.football_school_spring.controllers.AuthorizedUserController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coach")
class CoachController extends AuthorizedUserController {
}