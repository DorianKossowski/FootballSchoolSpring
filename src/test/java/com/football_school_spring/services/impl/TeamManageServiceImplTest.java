package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.services.TeamCreationService;
import com.football_school_spring.services.TeamManageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamManageServiceImplTest extends ServicesTests {
    @Autowired
    private TeamManageService underTest;
    @Autowired
    private TeamCreationService teamCreationService;
    @Autowired
    private CoachCreationService coachCreationService;
    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        Coach manager = new Coach();
        manager.setId(1L);
        manager.setMail("manager@mail.com");
        coachCreationService.createCoach(manager);

        teamCreationService.create(new Team(1L), manager, Arrays.asList("coach1@mail.com", "coach2@mail.com"));
    }

    @Test
    void shouldUpdateTeam() {
        // Given
        String newAddress = "newAddress";
        String newName = "newName";
        Team updatedTeam = new Team(1L);
        updatedTeam.setAddress(newAddress);
        updatedTeam.setName(newName);

        // When
        underTest.updateTeam(updatedTeam);

        // Then
        assertEquals(1, teamRepository.findAll().size());

        Team team = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(3, team.getTeamCoaches().size());
        assertEquals(newAddress, team.getAddress());
        assertEquals(newName, team.getName());
    }

    @Test
    void shouldDeleteCoach() {
        underTest.deleteCoachFromTeam(2L, 1L);

        assertEquals(1, teamRepository.findAll().size());
        Team team = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(2, team.getTeamCoaches().size());
    }

    @Test
    void shouldAssignNewCoaches() {
        underTest.assignNewCoaches(Arrays.asList("coach3@mail.com", "coach1@mail.com"), 1L);

        assertEquals(1, teamRepository.findAll().size());
        Team team = teamRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(4, team.getTeamCoaches().size());
    }
}