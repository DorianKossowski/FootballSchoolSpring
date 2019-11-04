package com.football_school_spring.services.impl;

import com.football_school_spring.models.Fixture;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.FixtureDTO;
import com.football_school_spring.repositories.FixtureRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.FixturesService;
import com.football_school_spring.services.ServicesTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FixturesServiceImplTest extends ServicesTests {
    @Autowired
    private FixturesService underTest;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private FixtureRepository fixtureRepository;

    @BeforeEach
    void setUp() {
        teamRepository.save(new Team("Name", "Address"));
    }

    @Test
    void shouldSaveFixture() {
        // Given
        FixtureDTO newFixture = new FixtureDTO(LocalDate.now(), LocalTime.of(0, 0));

        // When
        underTest.saveFixture(1L, newFixture);

        // Then
        assertEquals(1L, fixtureRepository.findAll().size());
    }

    @Test
    void shouldDeleteFixture() {
        // Given
        FixtureDTO newFixture = new FixtureDTO(LocalDate.now(), LocalTime.of(0, 0));
        fixtureRepository.save(new Fixture(teamRepository.findById(1L).orElseThrow(RuntimeException::new), newFixture));

        // When
        underTest.deleteFixture(1L);

        // Then
        assertEquals(0L, fixtureRepository.findAll().size());
    }

    @Test
    void shouldGetSortedFixtures() {
        // Given
        FixtureDTO fixture1 = new FixtureDTO(LocalDate.of(2019, 1, 10), LocalTime.of(0, 0));
        FixtureDTO fixture2 = new FixtureDTO(LocalDate.of(2019, 1, 5), LocalTime.of(0, 0));
        FixtureDTO fixture3 = new FixtureDTO(LocalDate.of(2019, 1, 10), LocalTime.of(1, 1));
        fixtureRepository.save(new Fixture(teamRepository.findById(1L).orElseThrow(RuntimeException::new), fixture1));
        fixtureRepository.save(new Fixture(teamRepository.findById(1L).orElseThrow(RuntimeException::new), fixture2));
        fixtureRepository.save(new Fixture(teamRepository.findById(1L).orElseThrow(RuntimeException::new), fixture3));

        // When
        List<Fixture> sortedTeamFixtures = underTest.getSortedTeamFixtures(1L);

        // Then
        assertEquals(3L, fixtureRepository.findAll().size());
        assertThat(sortedTeamFixtures).isSortedAccordingTo(Comparator.comparing(Fixture::getDate));
    }

    @Test
    void shouldGetNextFixture() {
        // Given
        FixtureDTO fixturePrev = new FixtureDTO(LocalDate.now().minusDays(1L), LocalTime.of(0, 0));
        FixtureDTO fixtureNext = new FixtureDTO(LocalDate.now().plusDays(1L), LocalTime.of(0, 0));
        fixtureRepository.save(new Fixture(teamRepository.findById(1L).orElseThrow(RuntimeException::new), fixturePrev));
        fixtureRepository.save(new Fixture(teamRepository.findById(1L).orElseThrow(RuntimeException::new), fixtureNext));

        // When
        Optional<Fixture> nextFixtureOptional = underTest.getNextFixture(1L);

        // Then
        assertTrue(nextFixtureOptional.isPresent());
        assertEquals(LocalDate.now().plusDays(1L), nextFixtureOptional.get().getDate().toLocalDate());
    }
}