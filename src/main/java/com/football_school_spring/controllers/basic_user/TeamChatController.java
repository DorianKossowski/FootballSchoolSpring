package com.football_school_spring.controllers.basic_user;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.CurrentTeamDTO;
import com.football_school_spring.services.ChatMessageService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class TeamChatController extends PossibleTeamsController {
    private static final Logger logger = getLogger(TeamChatController.class);

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/add-message")
    public String addMessage(Model model, @RequestParam("message") String message,
                             @SessionAttribute(CURRENT_TEAM) CurrentTeamDTO currentTeamDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String redirectUrl = user instanceof Coach ? "/coach/home" : "/parent/home";

        try {
            chatMessageService.addMessage(message, currentTeamDTO.getId(), user);
            logger.info("Message correctly added");
            return UrlCleaner.redirectWithCleaning(model, redirectUrl);
        } catch (Exception e) {
            logger.error("Error during adding message", e);
            return UrlCleaner.redirectWithCleaning(model, redirectUrl + "?error=true");
        }
    }
}