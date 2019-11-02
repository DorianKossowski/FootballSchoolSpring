package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.User;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.models.enums.UserTypeName;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.services.ServicesTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoachCreationServiceImplTest extends ServicesTests {
    @Autowired
    private CoachCreationService underTest;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldAddCoach() {
        underTest.createCoach(new Coach());

        List<User> allUsers = userRepository.findAll();
        assertEquals(1L, allUsers.size());

        User user = allUsers.iterator().next();
        assertEquals(UserStatusName.WAITING_FOR_APPROVAL.getName(), user.getUserStatus().getName());
        assertEquals(UserTypeName.COACH.getName(), user.getUserType().getName());
    }
}