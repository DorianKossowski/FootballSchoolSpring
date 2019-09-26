package com.football_school_spring.repositories;

import com.football_school_spring.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}