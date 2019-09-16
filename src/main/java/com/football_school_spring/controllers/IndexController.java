package com.football_school_spring.controllers;

import com.football_school_spring.models.ContactModel;
import com.football_school_spring.notifications.EmailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class IndexController {
    private static final Logger logger = getLogger(IndexController.class);

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("contactModel", new ContactModel());
        return "index";
    }

    @PostMapping(value = "/")
    public String contact(@ModelAttribute ContactModel contactModel) {
        try {
            emailService.sendContactMail(contactModel);
        } catch (Exception e) {
            logger.error("Problem during mail sending", e);
            return "redirect:/?error=true";
        }
        return "redirect:/?sent=true";
    }
}

