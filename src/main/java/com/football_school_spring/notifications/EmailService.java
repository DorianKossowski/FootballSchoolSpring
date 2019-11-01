package com.football_school_spring.notifications;

import com.football_school_spring.models.dto.ContactModelDTO;
import org.simplejavamail.email.Email;

public interface EmailService {
    void send(Email email);

    void sendContactMail(ContactModelDTO contactModelDTO);

    void sendRegistrationInviteMail(String mailTo, String registrationUrl);

    void sendMailWithResetLink(String mailTo, String resetLink);

    void sendMailWithTeamInvitation(String mailTo, String teamName);

    void sendMailWithBlockInfo(String mailTo);

    void sendMailWithFeeReminder(String mailTo);

    void sendMailWithNewPlayerAssigned(String mailTo);
}