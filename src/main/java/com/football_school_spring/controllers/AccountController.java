package com.football_school_spring.controllers;

import com.football_school_spring.controllers.basic_user.PossibleTeamsController;
import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Parent;
import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.EditPasswordDTO;
import com.football_school_spring.services.AccountService;
import com.football_school_spring.services.ObjectsDeletingService;
import com.football_school_spring.utils.UrlCleaner;
import com.football_school_spring.utils.exception.ManagerWithTeamException;
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
public class AccountController extends PossibleTeamsController {
    private static final Logger logger = getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private ObjectsDeletingService objectsDeletingService;

    @GetMapping("/account")
    public String getAccount(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("editPasswordObj", new EditPasswordDTO(user.getId()));
        if (user instanceof Coach) {
            model.addAttribute("hasTeams", !((Coach) user).getTeamCoaches().isEmpty());
            return "coach-account-edit";
        }
        if (user instanceof Parent) {
            return "parent-account-edit";
        }
        return "admin-account-edit";
    }

    @PostMapping("/account/edit")
    public String editAccount(Model model, @ModelAttribute User user) {
        try {
            accountService.updateAccount(user);
            // unnecessary to update panel with name of currently logged user
            updateSecurityContextHolder();
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

    @PostMapping("/coach/delete-account")
    public String deleteCoachAccount(Model model) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            objectsDeletingService.deleteCoach(user.getId());
            logger.info("Coach correctly deleted");
            return "redirect:/logout";
        } catch (ManagerWithTeamException e) {
            logger.error(e);
            return UrlCleaner.redirectWithCleaning(model, "/account?notDeleted=true");
        } catch (Exception e) {
            logger.error("Error during deleting coach", e);
            return UrlCleaner.redirectWithCleaning(model, "/account?error=true");
        }
    }

    @PostMapping("/parent/delete-account")
    public String deleteParentAccount(Model model) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            objectsDeletingService.deleteParent(user.getId());
            logger.info("Parent correctly deleted");
            return "redirect:/logout";
        } catch (Exception e) {
            logger.error("Error during deleting parent", e);
            return UrlCleaner.redirectWithCleaning(model, "/account?error=true");
        }
    }
}