package com.football_school_spring.services.impl;

import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.models.enums.UserTypeName;
import com.football_school_spring.services.ControllingFeesDatesService;
import com.football_school_spring.services.InitialDataLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements ApplicationRunner {
    private static final UserTypeName[] USER_TYPE_NAMES = UserTypeName.values();
    private static final UserStatusName[] USER_STATUS_NAMES = UserStatusName.values();
    private static final CoachPrivilegeName[] COACH_PRIVILEGE_NAMES = CoachPrivilegeName.values();

    @Autowired
    private InitialDataLoaderService initialDataLoaderService;
    @Autowired
    private ControllingFeesDatesService controllingFeesDatesService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userTypeInit();
        userStatusInit();
        coachPrivilegeInit();
        adminInit();
        controllingFeesDatesService.controlFees();
    }

    private void userTypeInit() {
        Arrays.stream(USER_TYPE_NAMES).forEach(name -> initialDataLoaderService.saveUserTypeIfNotExists(name.getName()));
    }

    private void userStatusInit() {
        Arrays.stream(USER_STATUS_NAMES).forEach(name -> initialDataLoaderService.saveUserStatusIfNotExists(name.getName()));
    }

    private void coachPrivilegeInit() {
        Arrays.stream(COACH_PRIVILEGE_NAMES).forEach(name -> initialDataLoaderService.saveCoachPrivilegeIfNotExists(name.getName()));
    }

    private void adminInit() {
        initialDataLoaderService.saveInitAdmin();
    }
}