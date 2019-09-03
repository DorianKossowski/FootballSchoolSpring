package com.football_school_spring.notifications.impl;

import com.football_school_spring.models.ContactModel;
import com.football_school_spring.notifications.EmailService;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Import(SimpleJavaMailSpringSupport.class)
public class EmailServiceImpl implements EmailService {
    @Autowired
    private Mailer mailer;
    @Autowired
    private Environment environment;

    @Override
    public void send(Email email) {
        mailer.sendMail(email);
    }

    @Override
    public void sendContactMail(ContactModel contactModel) {
        String adminMail = Objects.requireNonNull(environment.getProperty("simplejavamail.smtp.username"));
        Email email = EmailBuilder.startingBlank()
                .from(adminMail)
                .to(adminMail)
                .withSubject("New user interested in Football School")
                .withPlainText("Name: " + contactModel.getName())
                .appendText("\nMail: " + contactModel.getMail())
                .appendText("\nMessage: " + contactModel.getMessage())
                .buildEmail();
        send(email);
    }
}