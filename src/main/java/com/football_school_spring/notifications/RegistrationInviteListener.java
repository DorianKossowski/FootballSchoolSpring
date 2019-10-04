package com.football_school_spring.notifications;

import com.football_school_spring.models.User;
import com.football_school_spring.models.VerificationToken;
import com.football_school_spring.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationInviteListener implements ApplicationListener<OnRegistrationInviteEvent> {
    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void onApplicationEvent(OnRegistrationInviteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationInviteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setDefaultExpiryDate();

        verificationTokenRepository.save(verificationToken);
        String registrationUrl = "/register?token=" + token;
        emailService.sendRegistrationInviteMail(user.getMail(), registrationUrl);
    }
}