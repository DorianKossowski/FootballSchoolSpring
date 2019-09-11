package com.football_school_spring.notifications;

import com.football_school_spring.models.ContactModel;
import org.simplejavamail.email.Email;

public interface EmailService {
    void send(Email email);

    void sendContactMail(ContactModel contactModel);

    void sendRegistrationInviteMail(String mailTo, String registrationUrl);
}