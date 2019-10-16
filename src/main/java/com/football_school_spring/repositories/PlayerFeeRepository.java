package com.football_school_spring.repositories;

import com.football_school_spring.models.PlayerFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlayerFeeRepository extends JpaRepository<PlayerFee, Long> {
    List<PlayerFee> findByPlayerTeamId(Long teamId);

    Optional<PlayerFee> findByPlayerIdAndDate(Long playerId, LocalDate date);
}