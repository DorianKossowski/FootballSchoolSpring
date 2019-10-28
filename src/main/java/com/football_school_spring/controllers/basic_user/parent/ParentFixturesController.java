package com.football_school_spring.controllers.basic_user.parent;

import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.services.FixturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class ParentFixturesController extends ParentController {
    @Autowired
    private FixturesService fixturesService;

    @GetMapping("/fixtures")
    public String getFixtures(Model model, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        model.addAttribute("fixtures", fixturesService.getSortedTeamFixtures(currentTeamDTO.getId()));
        return "parent-fixtures";
    }
}