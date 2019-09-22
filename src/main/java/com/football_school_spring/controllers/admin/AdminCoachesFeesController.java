package com.football_school_spring.controllers.admin;

import com.football_school_spring.models.UserFeesListWrapper;
import com.football_school_spring.services.FeesService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class AdminCoachesFeesController extends AdminController {
    private static final Logger logger = getLogger(AdminCoachesFeesController.class);
    @Autowired
    private FeesService feesService;

    @GetMapping("/coaches-fees/{year}")
    public String coachesFees(Model model, @PathVariable("year") String year) {
        UserFeesListWrapper wrapper = new UserFeesListWrapper();
        wrapper.setFeesList(feesService.getCoachesFees(Integer.parseInt(year)));
        model.addAttribute("wrapper", wrapper);
        return "admin-coaches-fees";
    }

    @PostMapping("/coaches-fees/{year}")
    public String saveCoachesFees(Model model, @PathVariable("year") String year, @ModelAttribute UserFeesListWrapper wrapper) {
        try {
            feesService.setUpdatedFees(Integer.parseInt(year), wrapper);
            logger.info("Coaches fees updated correctly");
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-fees/" + year + "?saved=true");
        } catch (Exception e) {
            logger.error("Error during coaches fees update", e);
            return UrlCleaner.redirectWithCleaning(model, "/admin/coaches-fees/" + year + "?error=true");
        }
    }
}