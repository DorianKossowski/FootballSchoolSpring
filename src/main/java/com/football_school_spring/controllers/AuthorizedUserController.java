package com.football_school_spring.controllers;

import com.football_school_spring.models.User;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.utils.GettingFromDbException;
import com.football_school_spring.utils.SecurityContextHolderAuthenticationSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@Controller
public class AuthorizedUserController {
    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("userName")
    public String getUserName() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.ofNullable(user.getName()).orElse("") + " " + Optional.ofNullable(user.getSurname()).orElse("");
    }

    protected void updateSecurityContextHolder() {
        long loggedUserId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        SecurityContextHolderAuthenticationSetter.set(userRepository.findById(loggedUserId)
                .orElseThrow(() -> new GettingFromDbException(User.class)));
    }
}