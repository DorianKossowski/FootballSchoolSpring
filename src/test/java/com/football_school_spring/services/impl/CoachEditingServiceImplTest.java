package com.football_school_spring.services.impl;

import com.football_school_spring.WithoutInitAdminContextConfiguration;
import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.User;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.services.CoachEditingService;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.services.TeamCreationService;
import com.football_school_spring.utils.validation.CoachEditingValidationResult;
import com.football_school_spring.utils.validation.EditingValidationErrorName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(WithoutInitAdminContextConfiguration.class)
class CoachEditingServiceImplTest extends ServicesTests {
    @Autowired
    private CoachEditingService underTest;
    @Autowired
    private CoachCreationService coachCreationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private TeamCreationService teamCreationService;

    private Coach coachToAdd;

    @BeforeEach
    void setUp() {
        coachToAdd = new Coach(2L);
        coachToAdd.setId(1L);
        coachToAdd.setMail("coach@mail.com");
        coachCreationService.createCoach(coachToAdd);
    }

    @Test
    void shouldBeValidToEdit() {
        // Given
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setUserStatus(userStatusRepository.getByName(UserStatusName.ACTIVE.getName()));
        userRepository.save(user);

        // When
        CoachEditingValidationResult result = underTest.isEditingValid(1L);

        // Then
        assertTrue(result.isValid());
    }

    @Test
    void shouldBeInvalidToEditByStatus() {
        // When
        CoachEditingValidationResult result = underTest.isEditingValid(1L);

        // Then
        assertFalse(result.isValid());
        assertEquals(EditingValidationErrorName.NOT_ACTIVE.getName(), result.getErrorName().getName());
    }

    @Test
    void shouldBeInvalidToEditByExistence() {
        CoachEditingValidationResult result = underTest.isEditingValid(2L);

        assertFalse(result.isValid());
        assertEquals(EditingValidationErrorName.NOT_EXISTS.getName(), result.getErrorName().getName());
    }

    @Test
    void shouldBeValidToEditTeamsNumber() {
        // When
        Coach coach = (Coach) userRepository.findById(1L).orElseThrow(RuntimeException::new);
        CoachEditingValidationResult result = underTest.isTeamsNumberEditingValid(coach, 1);

        // Then
        assertTrue(result.isValid());
    }

    @Test
    void shouldBeInvalidToEditTeamsNumber() {
        // Given
        teamCreationService.create(new Team(), coachToAdd, Collections.emptyList());
        teamCreationService.create(new Team(), coachToAdd, Collections.emptyList());

        // When
        Coach coach = (Coach) userRepository.findById(1L).orElseThrow(RuntimeException::new);
        CoachEditingValidationResult result = underTest.isTeamsNumberEditingValid(coach, 1);

        // Then
        assertFalse(result.isValid());
        assertEquals(EditingValidationErrorName.WRONG_NUMBER.getName(), result.getErrorName().getName());
    }

    @Test
    void shouldBlockByChangingStatus() {
        // Given
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setUserStatus(userStatusRepository.getByName(UserStatusName.ACTIVE.getName()));
        userRepository.save(user);

        // When
        Coach coach = (Coach) userRepository.findById(1L).orElseThrow(RuntimeException::new);
        underTest.changeCoachStatus(coach);

        // Then
        User userAfterChanging = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(UserStatusName.BLOCKED.getName(), userAfterChanging.getUserStatus().getName());
    }

    @Test
    void shouldUnblockByChangingStatus() {
        // Given
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setUserStatus(userStatusRepository.getByName(UserStatusName.BLOCKED.getName()));
        userRepository.save(user);

        // When
        Coach coach = (Coach) userRepository.findById(1L).orElseThrow(RuntimeException::new);
        underTest.changeCoachStatus(coach);

        // Then
        User userAfterChanging = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(UserStatusName.ACTIVE.getName(), userAfterChanging.getUserStatus().getName());
    }

    @Test
    void shouldSetMaxNumberOfTeams() {
        // When
        Coach coach = (Coach) userRepository.findById(1L).orElseThrow(RuntimeException::new);
        underTest.setMaxNumberOfTeams(coach, 4);

        // Then
        Coach coachAfterEditing = (Coach) userRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(4, coachAfterEditing.getMaxNumberOfTeams());
    }
}