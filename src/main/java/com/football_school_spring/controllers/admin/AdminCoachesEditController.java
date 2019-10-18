package com.football_school_spring.controllers.admin;

import com.football_school_spring.models.Coach;
import com.football_school_spring.services.CoachEditingService;
import com.football_school_spring.utils.UrlCleaner;
import com.football_school_spring.utils.validation.CoachEditingValidationResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class AdminCoachesEditController extends AdminController {
    private static final Logger logger = getLogger(AdminCoachesEditController.class);

    @Autowired
    private CoachEditingService coachEditingService;

    @GetMapping("/coaches-edit/{coachId}")
    public String editCoach(Model model, @PathVariable("coachId") String coachId) {
        CoachEditingValidationResult result = coachEditingService.isEditingValid(Long.parseLong(coachId));
        if (result.isValid()) {
            model.addAttribute("coach", result.getCoach());
            return "admin-coaches-edit";
        }
        logger.error(result.getErrorMessage());
        return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-list?" + result.getErrorName().getUrlName());
    }

    @PostMapping("/coaches-edit-teams/{coachId}")
    public String editCoachNumberOfTeams(Model model, @PathVariable("coachId") String coachId, @RequestParam Map<String, String> attributes) {
        CoachEditingValidationResult result = coachEditingService.isEditingValid(Long.parseLong(coachId));
        if (!result.isValid()) {
            logger.error(result.getErrorMessage());
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-list?" + result.getErrorName().getUrlName());
        }

        Coach coach = result.getCoach();
        int newNumberOfTeams = Integer.parseInt(attributes.get("numOfTeams"));
        CoachEditingValidationResult numberResult = coachEditingService.isTeamsNumberEditingValid(coach, newNumberOfTeams);
        if (numberResult.isValid()) {
            coachEditingService.setMaxNumberOfTeams(coach, newNumberOfTeams);
            logger.info(String.format("Coach with id %s correctly edited", coachId));
            return UrlCleaner.redirectWithCleaning(model, String.format("/admin/coaches-edit/%s?edited=true", coachId));
        }
        logger.error(numberResult.getErrorMessage());
        return UrlCleaner.redirectWithCleaning(model, String.format("/admin/coaches-edit/%s?%s", coachId, numberResult.getErrorName().getUrlName()));
    }

    @PostMapping("/coaches-edit/{coachId}")
    public String editCoachStatus(Model model, @PathVariable("coachId") String coachId) {
        CoachEditingValidationResult result = coachEditingService.isEditingValid(Long.parseLong(coachId));
        if (!result.isValid()) {
            logger.error(result.getErrorMessage());
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-list?" + result.getErrorName().getUrlName());
        }
        coachEditingService.changeCoachStatus(result.getCoach());
        logger.info(String.format("Coach with id %s correctly edited", coachId));
        return UrlCleaner.redirectWithCleaning(model, String.format("/admin/coaches-edit/%s?edited=true", coachId));
    }
}