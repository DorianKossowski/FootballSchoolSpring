package com.football_school_spring.controllers;

import com.football_school_spring.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

public class AuthorizedUserController {
    @ModelAttribute("userName")
    public String getUserName() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.ofNullable(user.getName()).orElse("") + " " + Optional.ofNullable(user.getSurname()).orElse("");
    }
}