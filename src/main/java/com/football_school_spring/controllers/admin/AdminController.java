package com.football_school_spring.controllers.admin;

import com.football_school_spring.controllers.AuthorizedUserController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
class AdminController extends AuthorizedUserController {
}