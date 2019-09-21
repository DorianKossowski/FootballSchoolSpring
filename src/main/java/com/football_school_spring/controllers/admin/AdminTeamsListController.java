package com.football_school_spring.controllers.admin;

import com.football_school_spring.repositories.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminTeamsListController extends AdminController {
    @Autowired
    private CoachRepository coachRepository;

    @GetMapping("/teams-list")
    public String showCoachesList(Model model) {
        model.addAttribute("teamsSize", 0);
        return "admin-teams-list";
    }
}