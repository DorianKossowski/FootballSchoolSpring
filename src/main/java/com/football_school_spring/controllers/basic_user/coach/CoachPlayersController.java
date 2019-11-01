package com.football_school_spring.controllers.basic_user.coach;

import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.models.dto.NewPlayerDTO;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.services.PlayerCreationService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class CoachPlayersController extends CoachController {
    private static final Logger logger = getLogger(CoachPlayersController.class);

    @Autowired
    private PlayerCreationService playerCreationService;
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/players")
    public String getPlayers(Model model, @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        model.addAttribute("newPlayer", new NewPlayerDTO());
        model.addAttribute("players", playerRepository.findByTeamId(currentTeamDTO.getId()));
        return "coach-players";
    }

    @PostMapping("/player-add")
    public String addPlayer(Model model, @ModelAttribute("newPlayer") NewPlayerDTO newPlayerDTO,
                            @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        try {
            newPlayerDTO.setTeamId(currentTeamDTO.getId());
            playerCreationService.createPlayer(newPlayerDTO);

            logger.info("New player correctly added");
            return UrlCleaner.redirectWithCleaning(model, "/coach/players?sent=true");
        } catch (Exception e) {
            logger.error("Problem during coach invitation", e);
            return UrlCleaner.redirectWithCleaning(model, "/coach/players?error=true");
        }
    }
}