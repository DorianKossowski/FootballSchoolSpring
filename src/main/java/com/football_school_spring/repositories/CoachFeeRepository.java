package com.football_school_spring.repositories;

import com.football_school_spring.models.CoachFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CoachFeeRepository extends JpaRepository<CoachFee, Long> {
    Optional<CoachFee> findByUserIdAndDate(Long userId, LocalDate date);
}