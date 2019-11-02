package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.TeamCoach;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.services.CoachToTeamAttachingService;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.services.TeamCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class CoachToTeamAttachingServiceImplTest extends ServicesTests {
    @Autowired
    private CoachToTeamAttachingService underTest;
    @Autowired
    private TeamCreationService teamCreationService;
    @Autowired
    private CoachCreationService coachCreationService;
    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void SetUp() {
        Coach manager = new Coach(1L);
        manager.setId(1);
        manager.setMail("manager@mail.com");
        coachCreationService.createCoach(manager);
        teamCreationService.create(new Team(), manager, Collections.emptyList());
    }

    @Test
    void shouldAttachExistingCoach() {
        // Given
        Coach coach = new Coach();
        coach.setMail("coach@mail.com");
        coachCreationService.createCoach(coach);

        // When
        Team team = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        underTest.attach(team, "coach@mail.com");

        // Then
        Team teamAfterAttaching = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(2, teamAfterAttaching.getTeamCoaches().size());
        assertThat(teamAfterAttaching.getTeamCoaches().stream()
                .map(TeamCoach::getCoach)
                .map(Coach::getMail)
                .collect(Collectors.toList())
        ).contains("manager@mail.com", "coach@mail.com");
    }

    @Test
    void shouldAttachNewCoach() {
        // When
        Team team = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        underTest.attach(team, "coach@mail.com");

        // Then
        Team teamAfterAttaching = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(2, teamAfterAttaching.getTeamCoaches().size());
        assertThat(teamAfterAttaching.getTeamCoaches().stream()
                .map(TeamCoach::getCoach)
                .map(Coach::getMail)
                .collect(Collectors.toList())
        ).contains("manager@mail.com", "coach@mail.com");
    }

    @Test
    void shouldNotAttachCoachAlreadyManager() {
        // When
        Team team = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        underTest.attach(team, "manager@mail.com");

        // Then
        Team teamAfterAttaching = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(1, teamAfterAttaching.getTeamCoaches().size());
        assertThat(teamAfterAttaching.getTeamCoaches().stream()
                .map(TeamCoach::getCoach)
                .map(Coach::getMail)
                .collect(Collectors.toList())
        ).contains("manager@mail.com");
    }
}