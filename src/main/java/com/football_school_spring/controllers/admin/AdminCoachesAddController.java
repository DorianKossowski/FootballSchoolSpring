package com.football_school_spring.controllers.admin;

import com.football_school_spring.models.Coach;
import com.football_school_spring.notifications.OnRegistrationInviteEvent;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class AdminCoachesAddController extends AdminController {
    private static final Logger logger = getLogger(AdminCoachesAddController.class);

    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private CoachCreationService coachCreationService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/coaches-add")
    public String showCoachesList(Model model) {
        model.addAttribute("newCoach", new Coach());
        return "admin-coaches-add";
    }

    @PostMapping("/coaches-add")
    public String inviteNewCoach(@ModelAttribute Coach newCoach, Model model) {
        if (newCoach.getMaxNumberOfTeams() <= 0) {
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-add?minNumber=true");
        }
        try {
            coachCreationService.createCoach(newCoach);
            eventPublisher.publishEvent(new OnRegistrationInviteEvent(newCoach));
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-add?sent=true");
        } catch (Exception e) {
            logger.error("Problem during coach invitation", e);
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-add?error=true");
        }
    }
}