package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.models.enums.UserTypeName;
import com.football_school_spring.repositories.CoachPrivilegeRepository;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.repositories.UserTypeRepository;
import com.football_school_spring.services.CoachCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CoachCreationServiceImpl implements CoachCreationService {
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private CoachPrivilegeRepository coachPrivilegeRepository;
    @Autowired
    private CoachRepository coachRepository;

    @Override
    @Transactional
    public void createCoach(Coach coach, CoachPrivilegeName coachPrivilegeName) {
        coach.setUserStatus(userStatusRepository.getByName(UserStatusName.WAITING_FOR_APPROVAL.getName()));
        coach.setUserType(userTypeRepository.getByName(UserTypeName.COACH.getName()));
        coach.setCoachPrivilege(coachPrivilegeRepository.getByName(coachPrivilegeName.getName()));
        coachRepository.save(coach);
    }
}