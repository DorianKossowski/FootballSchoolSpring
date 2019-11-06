package com.football_school_spring.controllers;

import com.football_school_spring.notifications.EmailService;
import com.football_school_spring.services.ResetPasswordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

import static org.apache.log4j.Logger.getLogger;

@Controller
public class ResetPasswordController {
    private static final Logger logger = getLogger(ResetPasswordController.class);

    @Autowired
    private ResetPasswordService resetPasswordService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/forgotPassword")
    public String displayForgotPasswordPage() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam("mail") String userMail, HttpServletRequest request) {
        String token = UUID.randomUUID().toString();
        try {
            resetPasswordService.setResetPasswordToken(userMail, token);
        } catch (IllegalArgumentException e) {
            logger.error(String.format("User with mail %s doesn't exist", userMail));
            return "redirect:/forgotPassword?error=true";
        }
        emailService.sendMailWithResetLink(userMail, request.getScheme() + "://" + request.getServerName() + "/resetPassword?token=" + token);
        logger.info("Mail with reset link sent correctly");
        return "redirect:/forgotPassword?sent=true";
    }

    @GetMapping("/resetPassword")
    public String displayResetPasswordPage(Model model, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        if (resetPasswordService.isResetTokenExists(token)) {
            model.addAttribute("resetToken", token);
            return "resetPassword";
        } else {
            logger.error("Token doesn't exist");
            redirectAttributes.addFlashAttribute("badUser", "Reset password token doesn't exist");
            return "redirect:/?badUser=true";
        }
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam Map<String, String> requestParams, RedirectAttributes redirectAttributes) {
        String token = requestParams.get("token");
        String newPassword = requestParams.get("password");
        String repNewPassword = requestParams.get("repPassword");

        if (!newPassword.equals(repNewPassword)) {
            logger.error("Passwords are not equal");
            return "redirect:/resetPassword?token=" + token + "&error=true";
        }
        try {
            resetPasswordService.setNewPassword(token, newPassword);
        } catch (IllegalArgumentException e) {
            logger.error("Token doesn't exist");
            redirectAttributes.addFlashAttribute("badUser", "Reset password token doesn't exist");
            return "redirect:/?badUser=true";
        }
        logger.info("Password correctly changed");
        return "redirect:/login";
    }
}