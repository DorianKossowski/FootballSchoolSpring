package com.football_school_spring.services;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;

import java.util.List;

public interface TeamCreationService {

    void create(Team newTeam, Coach manager, List<String> coachesMails);
}