package com.football_school_spring.services.impl;

import com.football_school_spring.models.*;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.notifications.EmailService;
import com.football_school_spring.notifications.OnRegistrationInviteEvent;
import com.football_school_spring.repositories.CoachPrivilegeRepository;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.TeamCoachRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.services.TeamCreationService;
import com.football_school_spring.utils.SecurityContextHolderAuthenticationSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TeamCreationServiceImpl implements TeamCreationService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private CoachPrivilegeRepository coachPrivilegeRepository;
    @Autowired
    private TeamCoachRepository teamCoachRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CoachCreationService coachCreationService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void create(Team newTeam, List<String> coachesMails, WebRequest request) {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        teamRepository.save(newTeam);
        teamCoachRepository.save(new TeamCoach(new TeamCoachKey(newTeam.getId(), coach.getId(), coachPrivilegeRepository.getByName(CoachPrivilegeName.MANAGER.getName()).getId())));

        coachesMails.forEach(coachMail -> addCoachToTeam(request, newTeam, coachMail));

        // unnecessary to update number of teams of currently logged user
        SecurityContextHolderAuthenticationSetter.set(coachRepository.findById(coach.getId())
                .orElseThrow(() -> new IllegalArgumentException("Can't get coach from DB"))
        );

        sendTeamInvitationMails(coach.getMail(), coachesMails, newTeam.getName());
    }

    private void addCoachToTeam(WebRequest request, Team newTeam, String coachMail) {
        CoachPrivilege coachPrivilege = coachPrivilegeRepository.getByName(CoachPrivilegeName.COACH.getName());
        Optional<Coach> coachOptional = coachRepository.findByMail(coachMail);
        if (coachOptional.isPresent()) {
            Coach coachWithAccount = coachOptional.get();
            teamCoachRepository.save(new TeamCoach(new TeamCoachKey(newTeam.getId(), coachWithAccount.getId(), coachPrivilege.getId())));
        } else {
            Coach newCoach = new Coach(0);
            newCoach.setMail(coachMail);
            coachCreationService.createCoach(newCoach);
            eventPublisher.publishEvent(new OnRegistrationInviteEvent(newCoach, request.getContextPath()));
            teamCoachRepository.save(new TeamCoach(new TeamCoachKey(newTeam.getId(), newCoach.getId(), coachPrivilege.getId())));
        }
    }

    private void sendTeamInvitationMails(String managerMail, Collection<String> coachesMails, String teamName) {
        emailService.sendMailWithTeamInvitation(managerMail, teamName);
        coachesMails.forEach(coachMail -> emailService.sendMailWithTeamInvitation(coachMail, teamName));
    }
}