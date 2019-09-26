package com.football_school_spring.repositories;

import com.football_school_spring.models.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Long> {

    Optional<Coach> findByMail(String mail);
}