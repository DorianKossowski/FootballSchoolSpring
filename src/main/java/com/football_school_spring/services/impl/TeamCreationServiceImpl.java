package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.TeamCoach;
import com.football_school_spring.models.TeamCoachKey;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.notifications.EmailService;
import com.football_school_spring.repositories.CoachPrivilegeRepository;
import com.football_school_spring.repositories.TeamCoachRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.CoachToTeamAttachingService;
import com.football_school_spring.services.TeamCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class TeamCreationServiceImpl implements TeamCreationService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private CoachPrivilegeRepository coachPrivilegeRepository;
    @Autowired
    private TeamCoachRepository teamCoachRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CoachToTeamAttachingService coachToTeamAttachingService;

    @Override
    @Transactional
    public void create(Team newTeam, List<String> coachesMails) {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        teamRepository.save(newTeam);
        teamCoachRepository.save(new TeamCoach(new TeamCoachKey(newTeam.getId(), coach.getId(),
                coachPrivilegeRepository.getByName(CoachPrivilegeName.MANAGER.getName()).getId())));

        coachesMails.forEach(coachMail -> coachToTeamAttachingService.attach(newTeam, coachMail));

        sendTeamInvitationMails(coach.getMail(), coachesMails, newTeam.getName());
    }

    private void sendTeamInvitationMails(String managerMail, Collection<String> coachesMails, String teamName) {
        emailService.sendMailWithTeamInvitation(managerMail, teamName);
        coachesMails.forEach(coachMail -> emailService.sendMailWithTeamInvitation(coachMail, teamName));
    }
}