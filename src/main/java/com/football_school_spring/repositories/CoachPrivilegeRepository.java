package com.football_school_spring.repositories;

import com.football_school_spring.models.CoachPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoachPrivilegeRepository extends JpaRepository<CoachPrivilege, Long>, CommonEntitiesActions<CoachPrivilege> {
    Optional<CoachPrivilege> findByName(String name);
}