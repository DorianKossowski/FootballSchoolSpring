package com.football_school_spring.repositories;

import com.football_school_spring.models.TeamCoach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamCoachRepository extends JpaRepository<TeamCoach, Long> {
}