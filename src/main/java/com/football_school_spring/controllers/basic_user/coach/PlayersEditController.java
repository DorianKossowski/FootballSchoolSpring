package com.football_school_spring.controllers.basic_user.coach;

import com.football_school_spring.models.Player;
import com.football_school_spring.services.PlayerManageService;
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
public class PlayersEditController extends CoachController {
    private static final Logger logger = getLogger(PlayersEditController.class);

    @Autowired
    private PlayerManageService playerManageService;

    @GetMapping("/player-edit/{id}")
    public String getEditPlayer(Model model, @PathVariable("id") String playerId) {
        model.addAttribute("player", playerManageService.getPlayerById(Long.parseLong(playerId)));
        return "coach-edit-player";
    }

    @PostMapping("/player-edit/{id}")
    public String postEditPlayer(Model model, @ModelAttribute("player") Player editedPlayer) {
        try {
            playerManageService.update(editedPlayer);
            logger.info("Player correctly updated");
            return UrlCleaner.redirectWithCleaning(model, "/coach/player-edit/" + editedPlayer.getId() + "?updated=true");
        } catch (Exception e) {
            logger.error("Problem during editing player", e);
            return UrlCleaner.redirectWithCleaning(model, "/coach/player-edit/" + editedPlayer.getId() + "?error=true");
        }
    }

    @PostMapping("/delete-player/{id}")
    public String deleteCoach(Model model, @PathVariable("id") String playerId) {
        try {
            playerManageService.delete(Long.parseLong(playerId));
            logger.info("Player correctly deleted");
            return UrlCleaner.redirectWithCleaning(model, "/coach/players?deleted=true");
        } catch (Exception e) {
            logger.error("Problem during deleting player", e);
            return UrlCleaner.redirectWithCleaning(model, "/coach/players?error=true");
        }
    }
}