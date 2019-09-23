package com.football_school_spring.notifications.impl;

import com.football_school_spring.models.ContactModel;
import com.football_school_spring.notifications.EmailService;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

@Service
@Import(SimpleJavaMailSpringSupport.class)
public class EmailServiceImpl implements EmailService {
    @Autowired
    private Mailer mailer;

    @Value("${simplejavamail.smtp.username}")
    private String adminMail;

    @Override
    public void send(Email email) {
        mailer.sendMail(EmailBuilder.copying(email)
                .from(adminMail)
                .buildEmail()
        );
    }

    @Override
    public void sendContactMail(ContactModel contactModel) {
        Email email = EmailBuilder.startingBlank()
                .to(adminMail)
                .withSubject("New user interested in Football School")
                .withPlainText("Name: " + contactModel.getName())
                .appendText("\nMail: " + contactModel.getMail())
                .appendText("\nMessage: " + contactModel.getMessage())
                .buildEmail();
        send(email);
    }

    @Override
    public void sendRegistrationInviteMail(String mailTo, String registrationUrl) {
        Email email = EmailBuilder.startingBlank()
                .to(mailTo)
                .withSubject("Football School - registration")
                .appendText("http://localhost:8080" + registrationUrl)
                .buildEmail();
        send(email);
    }

    @Override
    public void sendMailWithResetLink(String mailTo, String resetLink) {
        Email email = EmailBuilder.startingBlank()
                .to(mailTo)
                .withSubject("Football School - reset password")
                .appendText("To reset your password, use below link:\n" + resetLink)
                .buildEmail();
        send(email);
    }
}