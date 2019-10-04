package com.football_school_spring.controllers.admin;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

import static com.football_school_spring.models.enums.UserStatusName.ACTIVE;
import static com.football_school_spring.models.enums.UserStatusName.BLOCKED;
import static org.apache.log4j.Logger.getLogger;

@Controller
public class AdminCoachesEditController extends AdminController {
    private static final Logger logger = getLogger(AdminCoachesEditController.class);
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;

    @GetMapping("/coaches-edit/{coachId}")
    public String editCoach(Model model, @PathVariable("coachId") String coachId) {
        Optional<Coach> coachOptional = coachRepository.findById(Long.valueOf(coachId));
        if (!coachOptional.isPresent()) {
            logger.error(String.format("Can't edit coach with id: %s - doesn't exist", coachId));
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-list?notExists=true");
        }

        Coach coach = coachOptional.get();
        if (coach.getUserStatus().getName().equals(UserStatusName.WAITING_FOR_APPROVAL.getName())) {
            logger.error(String.format("Can't edit coach with id: %s - doesn't active", coachId));
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-list?notActive=true");
        }
        model.addAttribute("coach", coach);
        return "admin-coaches-edit";
    }

    @PostMapping("/coaches-edit-teams/{coachId}")
    public String editCoachNumberOfTeams(Model model, @PathVariable("coachId") String coachId, @RequestParam Map<String, String> attributes) {
        Optional<Coach> coachOptional = coachRepository.findById(Long.valueOf(coachId));
        if (!coachOptional.isPresent()) {
            logger.error(String.format("Can't edit coach with id: %s - doesn't exist", coachId));
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-list?notExists=true");
        }

        Coach coach = coachOptional.get();
        long numberOfTeamsAsManager = coach.getTeamCoaches().stream()
                .filter(teamCoach -> teamCoach.getCoachPrivilege().getName().equals(CoachPrivilegeName.MANAGER.getName()))
                .count();
        int newNumberOfTeams = Integer.parseInt(attributes.get("numOfTeams"));
        if (numberOfTeamsAsManager > newNumberOfTeams) {
            logger.error(String.format("Can't edit coach with id: %s - too small number of teams", coachId));
            return UrlCleaner.redirectWithCleaning(model, String.format("/admin/coaches-edit/%s?wrongNumber=true", coachId));
        }
        coach.setMaxNumberOfTeams(newNumberOfTeams);
        coachRepository.save(coach);
        logger.info(String.format("Coach with id %s correctly edited", coachId));
        return UrlCleaner.redirectWithCleaning(model, String.format("/admin/coaches-edit/%s?edited=true", coachId));
    }

    @PostMapping("/coaches-edit/{coachId}")
    public String editCoachStatus(Model model, @PathVariable("coachId") String coachId) {
        Optional<Coach> coachOptional = coachRepository.findById(Long.valueOf(coachId));
        if (!coachOptional.isPresent()) {
            logger.error(String.format("Can't edit coach with id: %s - doesn't exist", coachId));
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-list?notExists=true");
        }

        Coach coach = coachOptional.get();
        if (coach.getUserStatus().getName().equals(ACTIVE.getName())) {
            coach.setUserStatus(userStatusRepository.getByName(BLOCKED.getName()));
        } else {
            coach.setUserStatus(userStatusRepository.getByName(ACTIVE.getName()));
        }
        coachRepository.save(coach);
        logger.info(String.format("Coach with id %s correctly edited", coachId));
        return UrlCleaner.redirectWithCleaning(model, String.format("/admin/coaches-edit/%s?edited=true", coachId));
    }
}