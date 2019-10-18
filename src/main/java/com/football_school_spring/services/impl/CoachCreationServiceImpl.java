package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.models.enums.UserTypeName;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.repositories.UserTypeRepository;
import com.football_school_spring.services.CoachCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class CoachCreationServiceImpl implements CoachCreationService {
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private CoachRepository coachRepository;

    @Override
    @Transactional
    public void createCoach(Coach coach) {
        coach.setUserStatus(userStatusRepository.getByName(UserStatusName.WAITING_FOR_APPROVAL.getName()));
        coach.setUserType(userTypeRepository.getByName(UserTypeName.COACH.getName()));
        coach.setDateOfCreation(LocalDateTime.now());
        coachRepository.save(coach);
    }
}