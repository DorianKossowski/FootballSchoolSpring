package com.football_school_spring.controllers;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.EditPasswordDTO;
import com.football_school_spring.models.User;
import com.football_school_spring.services.AccountService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class AccountController extends AuthorizedUserController {
    private static final Logger logger = getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping("/account")
    public String getAccount(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("editPasswordObj", new EditPasswordDTO(user.getId()));
        if (user instanceof Coach) {
            return "coach-account-edit";
        }
        return "admin-account-edit";
    }

    @PostMapping("/account/edit")
    public String editAccount(Model model, @ModelAttribute User user) {
        try {
            accountService.updateAccount(user);
            logger.info(String.format("User with id %d correctly updated", user.getId()));
            return UrlCleaner.redirectWithCleaning(model, "/account?edited=true");
        } catch (Exception e) {
            logger.error("Error during updating user", e);
            return UrlCleaner.redirectWithCleaning(model, "/account?error=true");
        }
    }

    @PostMapping("/account/edit-password")
    public String editPassword(Model model, @ModelAttribute EditPasswordDTO editPasswordObj) {
        try {
            accountService.changePassword(editPasswordObj);
            logger.info("Correctly updated password");
            return "redirect:/logout";
        } catch (Exception e) {
            logger.error("Error during changing user password", e);
            return UrlCleaner.redirectWithCleaning(model, "/account?error=true");
        }
    }
}