package com.football_school_spring.notifications.impl;

import com.football_school_spring.models.dto.ContactModelDTO;
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

    @Value("${app.url}")
    private String appUrl;

    @Override
    public void send(Email email) {
        mailer.sendMail(EmailBuilder.copying(email)
                .from(adminMail)
                .buildEmail()
        );
    }

    @Override
    public void sendContactMail(ContactModelDTO contactModelDTO) {
        Email emailToAdmin = EmailBuilder.startingBlank()
                .to(adminMail)
                .withSubject("New user interested in Football School")
                .withPlainText("Name: " + contactModelDTO.getName())
                .appendText("\nMail: " + contactModelDTO.getMail())
                .appendText("\nMessage: " + contactModelDTO.getMessage())
                .buildEmail();
        Email emailToClient = EmailBuilder.startingBlank()
                .to(contactModelDTO.getMail())
                .withSubject("Football School")
                .withPlainText("Thank you for interesting in Football School - our staff will contact you as soon as possible.")
                .appendText("\nYour message: " + contactModelDTO.getMessage())
                .buildEmail();

        send(emailToAdmin);
        send(emailToClient);
    }

    @Override
    public void sendRegistrationInviteMail(String mailTo, String registrationUrl) {
        Email email = EmailBuilder.startingBlank()
                .to(mailTo)
                .withSubject("Football School - registration")
                .appendText("Register to Football School using below link:\n")
                .appendText(appUrl + registrationUrl)
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

    @Override
    public void sendMailWithTeamInvitation(String mailTo, String teamName) {
        Email email = EmailBuilder.startingBlank()
                .to(mailTo)
                .withSubject("Football School - you have been added to new team")
                .appendText(String.format("From now on, you are coach of %s. " +
                        "Visit Football School to start manage your team.", teamName))
                .buildEmail();
        send(email);
    }

    @Override
    public void sendMailWithBlockInfo(String mailTo) {
        Email email = EmailBuilder.startingBlank()
                .to(mailTo)
                .withSubject("Football School - you have been blocked")
                .appendText("From now on, you are not allowed to use Football School application.\n" +
                        "Contact administrator for more information.")
                .buildEmail();
        Email toAdmin = EmailBuilder.startingBlank()
                .to(adminMail)
                .withSubject("Football School - user blocked")
                .appendText(String.format("User with mail %s has been blocked", mailTo))
                .buildEmail();
        send(email);
        send(toAdmin);
    }

    @Override
    public void sendMailWithFeeReminder(String mailTo) {
        Email email = EmailBuilder.startingBlank()
                .to(mailTo)
                .withSubject("Football School - fee reminder")
                .appendText("Football School reminds you to pay service fees.")
                .buildEmail();
        send(email);
    }

    @Override
    public void sendMailWithNewPlayerAssigned(String mailTo) {
        Email email = EmailBuilder.startingBlank()
                .to(mailTo)
                .withSubject("Football School - new player assigned")
                .appendText("You have been assigned to new player.")
                .buildEmail();
        send(email);
    }
}