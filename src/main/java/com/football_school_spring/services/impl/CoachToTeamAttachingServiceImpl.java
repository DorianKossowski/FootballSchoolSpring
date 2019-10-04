package com.football_school_spring.services.impl;

import com.football_school_spring.models.*;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.notifications.OnRegistrationInviteEvent;
import com.football_school_spring.repositories.CoachPrivilegeRepository;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.TeamCoachRepository;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.services.CoachToTeamAttachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CoachToTeamAttachingServiceImpl implements CoachToTeamAttachingService {
    @Autowired
    private CoachPrivilegeRepository coachPrivilegeRepository;
    @Autowired
    private TeamCoachRepository teamCoachRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private CoachCreationService coachCreationService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void attach(Team team, String coachMail) {
        CoachPrivilege coachPrivilege = coachPrivilegeRepository.getByName(CoachPrivilegeName.COACH.getName());
        Optional<Coach> coachOptional = coachRepository.findByMail(coachMail);
        if (coachOptional.isPresent()) {
            Coach coachWithAccount = coachOptional.get();
            teamCoachRepository.save(new TeamCoach(new TeamCoachKey(team.getId(), coachWithAccount.getId(), coachPrivilege.getId())));
        } else {
            Coach newCoach = new Coach(0);
            newCoach.setMail(coachMail);
            coachCreationService.createCoach(newCoach);
            eventPublisher.publishEvent(new OnRegistrationInviteEvent(newCoach));
            teamCoachRepository.save(new TeamCoach(new TeamCoachKey(team.getId(), newCoach.getId(), coachPrivilege.getId())));
        }
    }
}