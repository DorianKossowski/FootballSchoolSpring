package com.football_school_spring.controllers.basic_user.coach;

import com.football_school_spring.models.dto.PlayerFeesDTOListWrapper;
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
public class CoachPlayersFeesController extends CoachController {
    private static final Logger logger = getLogger(CoachPlayersFeesController.class);
    @Autowired
    private FeesService feesService;

    @GetMapping("/players-fees/{teamId}/{year}")
    public String playersFees(Model model, @PathVariable("teamId") String teamId, @PathVariable("year") String year) {
        PlayerFeesDTOListWrapper wrapper = new PlayerFeesDTOListWrapper();
        wrapper.setFeesList(feesService.getPlayersFees(Integer.parseInt(teamId), Integer.parseInt(year)));
        model.addAttribute("wrapper", wrapper);
        return "coach-fees";
    }

    @PostMapping("/players-fees/{teamId}/{year}")
    public String savePlayersFees(Model model, @PathVariable("teamId") String teamId, @PathVariable("year") String year,
                                  @ModelAttribute PlayerFeesDTOListWrapper wrapper) {
        try {
            feesService.setUpdatedPlayersFees(Integer.parseInt(year), wrapper);
            logger.info("Players fees updated correctly");
            return UrlCleaner.redirectWithCleaning(model, String.format("/coach/players-fees/%s/%s?saved=true", teamId, year));
        } catch (Exception e) {
            logger.error("Error during coaches fees update", e);
            return UrlCleaner.redirectWithCleaning(model, String.format("/coach/players-fees/%s/%s?error=true", teamId, year));
        }
    }
}