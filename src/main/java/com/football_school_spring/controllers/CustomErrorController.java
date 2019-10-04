package com.football_school_spring.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "error";
    }

    @PostMapping("/error")
    public String postError() {
        return "redirect:/error";
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}