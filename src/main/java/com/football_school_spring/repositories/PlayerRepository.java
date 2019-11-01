package com.football_school_spring.repositories;

import com.football_school_spring.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeamId(Long teamId);

    List<Player> findByParentId(Long parentId);

    List<Player> findByParentIdAndTeamId(Long parentId, Long teamId);

    Optional<Player> findByIdAndParentId(Long id, Long parentId);

    Optional<Player> findByIdAndTeamId(Long id, Long teamId);
}