package com.football_school_spring.services;

import com.football_school_spring.models.Team;

import java.util.List;

public interface TeamCreationService {

    void create(Team newTeam, List<String> coachesMails);
}