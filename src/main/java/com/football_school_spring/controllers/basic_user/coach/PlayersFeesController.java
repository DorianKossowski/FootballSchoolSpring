package com.football_school_spring.controllers.basic_user.coach;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.models.dto.PlayerFeesDTOListWrapper;
import com.football_school_spring.services.FeesService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class PlayersFeesController extends CoachController {
    private static final Logger logger = getLogger(PlayersFeesController.class);
    @Autowired
    private FeesService feesService;

    @GetMapping("/players-fees/{teamId}/{year}")
    public String playersFees(Model model, @PathVariable("teamId") String teamId, @PathVariable("year") String year,
                              @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!isManager(coach, currentTeamDTO.getId())) {
            return UrlCleaner.redirectWithCleaning(model, "/coach/home?notManager");
        }
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