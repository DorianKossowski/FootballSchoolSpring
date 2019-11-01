package com.football_school_spring.controllers.basic_user.parent;

import com.football_school_spring.models.Player;
import com.football_school_spring.models.User;
import com.football_school_spring.services.PlayerManageService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class ParentPlayersEditController extends ParentController {
    private static final Logger logger = getLogger(ParentPlayersEditController.class);

    @Autowired
    private PlayerManageService playerManageService;

    @GetMapping("/player-edit/{id}")
    public String getEditPlayer(Model model, @PathVariable("id") String playerId) {
        model.addAttribute("player", playerManageService.getPlayerByIdAndParentId(Long.parseLong(playerId),
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
        return "parent-edit-player";
    }

    @PostMapping("/player-edit/{id}")
    public String postEditPlayer(Model model, @ModelAttribute("player") Player editedPlayer) {
        try {
            playerManageService.update(editedPlayer);
            logger.info("Player correctly updated");
            return UrlCleaner.redirectWithCleaning(model, "/parent/player-edit/" + editedPlayer.getId() + "?updated=true");
        } catch (Exception e) {
            logger.error("Problem during editing player", e);
            return UrlCleaner.redirectWithCleaning(model, "/parent/player-edit/" + editedPlayer.getId() + "?error=true");
        }
    }
}