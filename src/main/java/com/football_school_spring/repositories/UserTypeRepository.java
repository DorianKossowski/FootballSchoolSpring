package com.football_school_spring.repositories;

import com.football_school_spring.models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType, Long>, CommonEntitiesActions<UserType> {
}