package com.football_school_spring.repositories;

import com.football_school_spring.models.CoachPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachPrivilegeRepository extends JpaRepository<CoachPrivilege, Long>, CommonEntitiesActions<CoachPrivilege> {
}