package com.football_school_spring.repositories;

import com.football_school_spring.models.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FeeRepository extends JpaRepository<Fee, Long> {
    Optional<Fee> findByUserIdAndDate(Long userId, LocalDate date);
}