package com.football_school_spring.controllers.admin;

import com.football_school_spring.models.dto.CoachesListDTO;
import com.football_school_spring.repositories.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminCoachesListController extends AdminController {
    @Autowired
    private CoachRepository coachRepository;

    @GetMapping("/coaches-list")
    public String showCoachesList(Model model) {
        List<CoachesListDTO> coaches = coachRepository.findAll().stream()
                .map(CoachesListDTO::new)
                .collect(Collectors.toList());
        model.addAttribute("coaches", coaches);

        return "admin-coaches-list";
    }
}