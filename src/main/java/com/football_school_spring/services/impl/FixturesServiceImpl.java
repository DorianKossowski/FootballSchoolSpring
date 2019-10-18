package com.football_school_spring.services.impl;

import com.football_school_spring.models.Fixture;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.FixtureDTO;
import com.football_school_spring.repositories.FixtureRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.FixturesService;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FixturesServiceImpl implements FixturesService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private FixtureRepository fixtureRepository;

    @Override
    public void saveFixture(long teamId, FixtureDTO newFixture) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new GettingFromDbException(Team.class, teamId));
        fixtureRepository.save(new Fixture(team, newFixture));
    }

    @Override
    public void deleteFixture(long id) {
        fixtureRepository.deleteById(id);
    }

    @Override
    public List<Fixture> getSortedTeamFixtures(long teamId) {
        return fixtureRepository.findByTeamId(teamId).stream()
                .sorted(Comparator.comparing(Fixture::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Fixture> getNextFixture(long teamId) {
        List<Fixture> fixtures = getSortedTeamFixtures(teamId);
        return fixtures.stream()
                .filter(fixture -> fixture.getDate().isAfter(LocalDateTime.now()))
                .findFirst();
    }
}