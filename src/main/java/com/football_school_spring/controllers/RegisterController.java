package com.football_school_spring.controllers;

import com.football_school_spring.models.VerificationToken;
import com.football_school_spring.models.dto.UserRegistrationDTO;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.repositories.VerificationTokenRepository;
import com.football_school_spring.services.UserRegistrationService;
import com.football_school_spring.utils.UrlCleaner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Optional;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class RegisterController {
    private static final Logger logger = getLogger(RegisterController.class);

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserRegistrationService userRegistrationService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/register")
    public String register(Model model, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        if (!verificationTokenOptional.isPresent()) {
            logger.warn("Token doesn't exist");
            redirectAttributes.addFlashAttribute("badUser", "Registration token doesn't exist");
            return "redirect:/?badUser=true";
        }

        VerificationToken verificationToken = verificationTokenOptional.get();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            logger.warn("Token's already expired");
            verificationTokenRepository.delete(verificationToken);
            userRepository.delete(verificationToken.getUser());
            model.addAttribute("badUser", "Registration token's already expired");
            return "redirect:/?badUser=true";
        }

        model.addAttribute("newUserMail", verificationToken.getUser().getMail());
        model.addAttribute("newUser", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping(value = "/register")
    public String postRegister(Model model, @RequestParam("token") String token, @ModelAttribute(name = "newUserMail") String mail,
                               @Valid UserRegistrationDTO newUser) {
        try {
            userRegistrationService.registerUser(mail, newUser);
            logger.info("New user correctly added");
            return UrlCleaner.redirectWithCleaning(model, "/login");
        } catch (Exception e) {
            logger.error("Problem occurred during registration new user", e);
            return UrlCleaner.redirectWithCleaning(model, "/register?token=" + token + "&error=true");
        }
    }
}