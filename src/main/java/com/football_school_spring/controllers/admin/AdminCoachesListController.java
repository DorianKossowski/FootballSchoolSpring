package com.football_school_spring.controllers.admin;

import com.football_school_spring.models.Coach;
import com.football_school_spring.repositories.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminCoachesListController extends AdminController {
    @Autowired
    private CoachRepository coachRepository;

    @GetMapping("/coaches")
    public String showCoachesList(Model model) {
        List<Coach> coaches = coachRepository.findAll();

        model.addAttribute("coaches", coaches);
        model.addAttribute("coachesSize", coaches.size());
        return "admin-coaches";
    }
}