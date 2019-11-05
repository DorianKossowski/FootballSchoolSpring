package com.football_school_spring.repositories;

import com.football_school_spring.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long>, CommonEntitiesActions<UserStatus> {
    Optional<UserStatus> findByName(String name);
}