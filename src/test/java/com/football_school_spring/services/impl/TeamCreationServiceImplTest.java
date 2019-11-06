package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.notifications.EmailService;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.services.TeamCreationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class TeamCreationServiceImplTest extends ServicesTests {
    @Autowired
    private TeamCreationService underTest;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CoachCreationService coachCreationService;

    @Test
    void shouldCreateTeamWithoutAdditionCoaches() {
        // Given
        Coach manager = new Coach();
        manager.setId(1L);
        manager.setMail("manager@mail.com");
        coachCreationService.createCoach(manager);

        // When
        underTest.create(new Team(1L), manager, Collections.emptyList());

        // Then
        assertEquals(1, teamRepository.findAll().size());
        assertEquals(1, userRepository.findAll().size());

        Team team = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(1, team.getTeamCoaches().size());
        assertEquals("manager@mail.com", team.getManager().getMail());
        Mockito.verify(emailService, Mockito.times(1)).sendMailWithTeamInvitation(any(), any());
    }

    @Test
    void shouldCreateTeamWithAdditionCoaches() {
        // Given
        Coach manager = new Coach();
        manager.setId(1L);
        manager.setMail("manager@mail.com");
        coachCreationService.createCoach(manager);

        // When
        underTest.create(new Team(1L), manager, Arrays.asList("coach1@mail.com", "coach2@mail.com"));

        // Then
        assertEquals(1, teamRepository.findAll().size());
        assertEquals(3, userRepository.findAll().size());


        Team team = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(3, team.getTeamCoaches().size());
        assertEquals("manager@mail.com", team.getManager().getMail());
        Mockito.verify(emailService, Mockito.times(3)).sendMailWithTeamInvitation(any(), any());
    }
}