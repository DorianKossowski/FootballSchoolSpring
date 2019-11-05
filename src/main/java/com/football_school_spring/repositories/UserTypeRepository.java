package com.football_school_spring.repositories;

import com.football_school_spring.models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTypeRepository extends JpaRepository<UserType, Long>, CommonEntitiesActions<UserType> {
    Optional<UserType> findByName(String name);
}