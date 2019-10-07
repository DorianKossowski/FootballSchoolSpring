package com.football_school_spring.services;

import com.football_school_spring.models.Fixture;
import com.football_school_spring.models.dto.FixtureDTO;

import java.util.List;
import java.util.Optional;

public interface FixturesService {
    void saveFixture(long teamId, FixtureDTO newFixture);

    void deleteFixture(long id);

    List<Fixture> getSortedTeamFixtures(long teamId);

    Optional<Fixture> getNextFixture(long teamId);
}