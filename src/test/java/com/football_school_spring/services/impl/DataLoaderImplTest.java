package com.football_school_spring.services.impl;

import com.football_school_spring.FootballSchoolApplicationTests;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.models.enums.UserTypeName;
import com.football_school_spring.repositories.CoachPrivilegeRepository;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.repositories.UserTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataLoaderImplTest extends FootballSchoolApplicationTests {
    @Autowired
    private DataLoaderImpl underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private CoachPrivilegeRepository coachPrivilegeRepository;

    @Value("${admin.mail}")
    private String adminMail;

    @Test
    void shouldLoad() throws Exception {
        // When
        underTest.run(new DefaultApplicationArguments(new String[]{}));

        // Then
        assertEquals(1, userRepository.findAll().size());
        assertTrue(userRepository.findByMail(adminMail).isPresent());

        assertEquals(3, userTypeRepository.findAll().size());
        Arrays.stream(UserTypeName.values()).forEach(userTypeName ->
                assertTrue(userTypeRepository.findByName(userTypeName.getName()).isPresent())
        );

        assertEquals(3, userStatusRepository.findAll().size());
        Arrays.stream(UserStatusName.values()).forEach(userStatusName ->
                assertTrue(userStatusRepository.findByName(userStatusName.getName()).isPresent())
        );

        assertEquals(2, coachPrivilegeRepository.findAll().size());
        Arrays.stream(CoachPrivilegeName.values()).forEach(coachPrivilegeName ->
                assertTrue(coachPrivilegeRepository.findByName(coachPrivilegeName.getName()).isPresent())
        );
    }
}