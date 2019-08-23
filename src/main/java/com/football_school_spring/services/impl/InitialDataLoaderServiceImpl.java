package com.football_school_spring.services.impl;

import com.football_school_spring.models.CoachPrivilege;
import com.football_school_spring.models.UserStatus;
import com.football_school_spring.models.UserType;
import com.football_school_spring.repositories.CoachPrivilegeRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.repositories.UserTypeRepository;
import com.football_school_spring.services.InitialAdminLoader;
import com.football_school_spring.services.InitialDataLoaderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.apache.log4j.Logger.getLogger;

@Service
public class InitialDataLoaderServiceImpl implements InitialDataLoaderService {
    private static final Logger logger = getLogger(InitialDataLoaderServiceImpl.class);

    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private CoachPrivilegeRepository coachPrivilegeRepository;
    @Autowired
    private InitialAdminLoader initialAdminLoader;

    @Override
    public void saveUserTypeIfNotExists(String userTypeName) {
        if (!userTypeRepository.existsByName(userTypeName)) {
            userTypeRepository.save(new UserType(userTypeName));
            logger.debug(String.format("Saved user type: %s", userTypeName));
        }
    }

    @Override
    public void saveUserStatusIfNotExists(String userStatusName) {
        if (!userStatusRepository.existsByName(userStatusName)) {
            userStatusRepository.save(new UserStatus(userStatusName, new Date()));
            logger.debug(String.format("Saved user status: %s", userStatusName));
        }
    }

    @Override
    public void saveCoachPrivilegeIfNotExists(String coachPrivilegeName) {
        if (!coachPrivilegeRepository.existsByName(coachPrivilegeName)) {
            coachPrivilegeRepository.save(new CoachPrivilege(coachPrivilegeName));
            logger.debug(String.format("Saved coach privilege: %s", coachPrivilegeName));
        }
    }

    @Override
    public void saveInitAdmin() {
        initialAdminLoader.save();
    }
}