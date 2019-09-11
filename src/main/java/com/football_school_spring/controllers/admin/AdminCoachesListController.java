package com.football_school_spring.controllers.admin;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.notifications.OnRegistrationInviteEvent;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.services.CoachCreationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class AdminCoachesListController extends AdminController {
    private static final Logger logger = getLogger(AdminCoachesListController.class);

    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private CoachCreationService coachCreationService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/coaches")
    public String showCoachesList(Model model) {
        List<Coach> coaches = coachRepository.findAll();
        model.addAttribute("coaches", coaches);
        model.addAttribute("coachesSize", coaches.size());
        model.addAttribute("newCoach", new Coach());
        return "admin-coaches";
    }

    @PostMapping("/coaches")
    public String inviteNewCoach(@ModelAttribute Coach newCoach, WebRequest request, Model model) {
        try {
            coachCreationService.createCoach(newCoach, CoachPrivilegeName.MANAGER);
            eventPublisher.publishEvent(new OnRegistrationInviteEvent(newCoach, request.getContextPath()));
            return "redirect:/admin/coaches?sent=true";
        } catch (Exception e) {
            logger.error("Problem during coach invitation", e);
            return "redirect:/admin/coaches?error=true";
        } finally {
            // to remove userName attribute from URL
            model.asMap().clear();
        }
    }
}