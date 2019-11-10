package com.football_school_spring.services;

import com.football_school_spring.models.Player;

public interface PlayerManageService {

    Player getPlayerByIdAndParentId(long id, long parentId);

    Player getPlayerByIdAndTeamId(long id, long teamId);

    void update(Player player);
}