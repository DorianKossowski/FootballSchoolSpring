package com.football_school_spring.services.impl;

import com.football_school_spring.models.*;
import com.football_school_spring.repositories.*;
import com.football_school_spring.services.ObjectsDeletingService;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.utils.exception.ManagerWithTeamException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjectsDeletingServiceImplTest extends ServicesTests {
    @Autowired
    private ObjectsDeletingService underTest;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private FixtureRepository fixtureRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerFeeRepository playerFeeRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private TeamCoachRepository teamCoachRepository;
    @Autowired
    private CoachFeeRepository coachFeeRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldDeleteTeam() {
        // Given
        Team team = new Team(1L);
        Team team2 = new Team(2L);
        teamRepository.saveAll(Arrays.asList(team, team2));

        Fixture fixture = new Fixture();
        fixture.setTeam(team);
        fixtureRepository.save(fixture);

        Player player = new Player();
        player.setTeam(team);
        Player player2 = new Player();
        player2.setTeam(team2);
        playerRepository.saveAll(Arrays.asList(player, player2));


        // When
        underTest.deleteTeam(1L);

        // Then
        assertEquals(1, teamRepository.findAll().size());
        assertEquals(0, fixtureRepository.findAll().size());
        assertEquals(1, playerRepository.findAll().size());
    }

    @Test
    void shouldDeletePlayer() {
        // Given
        Team team = new Team(1L);
        teamRepository.save(team);

        Player player = new Player();
        player.setTeam(team);
        Player player2 = new Player();
        player2.setTeam(team);
        playerRepository.saveAll(Arrays.asList(player, player2));

        playerFeeRepository.saveAll(Arrays.asList(
                new PlayerFee(player, LocalDate.now()),
                new PlayerFee(player2, LocalDate.now())
        ));


        // When
        underTest.deletePlayer(1L);

        // Then
        assertEquals(1, teamRepository.findAll().size());
        assertEquals(1, playerRepository.findAll().size());
        assertEquals(1, playerFeeRepository.findAll().size());
    }

    @Test
    void shouldDeleteCoach() {
        // Given
        Team team = new Team(1L);
        teamRepository.save(team);

        Coach coach = new Coach();
        coach.setId(1L);
        Coach coach2 = new Coach();
        coach2.setId(2L);
        coachRepository.saveAll(Arrays.asList(coach, coach2));

        // assign as coach
        teamCoachRepository.saveAll(Arrays.asList(
                new TeamCoach(new TeamCoachKey(1L, 1L, 2L)),
                new TeamCoach(new TeamCoachKey(1L, 2L, 2L))
        ));

        coachFeeRepository.saveAll(Arrays.asList(
                new CoachFee(coach, LocalDate.now()),
                new CoachFee(coach2, LocalDate.now())
        ));

        // When
        underTest.deleteCoach(1L);

        // Then
        assertEquals(1, teamRepository.findAll().size());
        assertEquals(1, coachRepository.findAll().size());
        assertEquals(1, coachFeeRepository.findAll().size());
    }

    @Test
    void shouldThrowDuringDeletingCoach() {
        Team team = new Team(1L);
        teamRepository.save(team);

        Coach coach = new Coach();
        coach.setId(1L);
        coachRepository.save(coach);

        // assign as manager
        teamCoachRepository.save(new TeamCoach(new TeamCoachKey(1L, 1L, 1L)));

        assertThrows(ManagerWithTeamException.class, () -> underTest.deleteCoach(1L));
    }

    @Test
    void shouldDeleteParent() {
        // Given
        Parent parent = new Parent();
        parent.setId(1L);
        parentRepository.save(parent);

        Player player = new Player();
        player.setId(1L);
        player.setParent(parent);
        Player player2 = new Player();
        player2.setId(2L);
        player2.setParent(parent);
        playerRepository.saveAll(Arrays.asList(player, player2));

        // When
        underTest.deleteParent(1L);

        // Then
        assertEquals(0, parentRepository.findAll().size());
        List<Player> leftPlayers = playerRepository.findAll().stream()
                .filter(playerFromDB -> playerFromDB.getParent() == null)
                .collect(Collectors.toList());
        assertEquals(2, leftPlayers.size());
    }

    @Test
    void shouldDeleteUsers() {
        // Given
        Parent parent = new Parent();
        parent.setId(1L);
        parentRepository.save(parent);

        Coach coach = new Coach();
        coach.setId(2L);
        coachRepository.save(coach);

        // When
        underTest.deleteUser(1L);
        underTest.deleteUser(2L);

        // Then
        assertEquals(0, userRepository.findAll().size());
    }
}