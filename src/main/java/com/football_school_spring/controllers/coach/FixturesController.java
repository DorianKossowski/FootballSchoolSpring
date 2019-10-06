package com.football_school_spring.controllers.coach;

import com.football_school_spring.models.Fixture;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.models.dto.FixtureDTO;
import com.football_school_spring.services.FixturesService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class FixturesController extends CoachController {
    private static final Logger logger = getLogger(FixturesController.class);

    @Autowired
    private FixturesService fixturesService;

    @GetMapping("/fixtures")
    public String getFixtures(Model model, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        FixtureDTO newFixture = new FixtureDTO(currentTeamDTO.getAddress());
        List<Fixture> fixtures = fixturesService.getSortedTeamFixtures(currentTeamDTO.getId());

        model.addAttribute("newFixture", newFixture);
        model.addAttribute("fixtures", fixtures);
        return "coach-fixtures";
    }

    @PostMapping("fixture-add")
    public String postFixtures(Model model, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO,
                               @ModelAttribute FixtureDTO newFixture) {
        try {
            fixturesService.saveFixture(currentTeamDTO.getId(), newFixture);
            logger.info("New fixture correctly added");
            return UrlCleaner.redirectWithCleaning(model, "/coach/fixtures");
        } catch (Exception e) {
            logger.error("Error during fixture adding");
            return UrlCleaner.redirectWithCleaning(model, "/coach/fixtures?error=true");
        }
    }

    @PostMapping("/fixture-delete/{fixtureId}")
    public String deleteFixture(Model model, @PathVariable("fixtureId") String fixtureId) {
        try {
            fixturesService.deleteFixture(Long.parseLong(fixtureId));
            logger.info(String.format("Fixture with id %s correctly deleted", fixtureId));
            return UrlCleaner.redirectWithCleaning(model, "/coach/fixtures");
        } catch (Exception e) {
            logger.error("Error during fixture adding");
            return UrlCleaner.redirectWithCleaning(model, "/coach/fixtures?error=true");
        }
    }
}