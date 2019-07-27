package com.football_school_spring.controllers;

import com.football_school_spring.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping(value = "/")
    public String registry(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "index";
    }

}

