package com.football_school_spring.services.impl;

import com.football_school_spring.FootballSchoolApplicationTests;
import com.football_school_spring.repositories.CoachPrivilegeRepository;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.repositories.UserTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void shouldLoad() throws Exception {
        // When
        underTest.run(new DefaultApplicationArguments(new String[]{}));

        // Then
        assertEquals(1, userRepository.findAll().size());
        assertEquals(3, userTypeRepository.findAll().size());
        assertEquals(3, userStatusRepository.findAll().size());
        assertEquals(2, coachPrivilegeRepository.findAll().size());
    }
}