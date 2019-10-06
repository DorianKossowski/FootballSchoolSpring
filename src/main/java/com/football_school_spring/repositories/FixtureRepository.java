package com.football_school_spring.repositories;

import com.football_school_spring.models.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    List<Fixture> findByTeamId(Long teamId);
}