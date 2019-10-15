package com.football_school_spring.services;

import com.football_school_spring.models.Player;

public interface PlayerManageService {
    Player getPlayerById(long id);

    void update(Player player);

    void delete(long playerId);
}