package com.football_school_spring.services;

import com.football_school_spring.models.dto.NewPlayerDTO;

public interface PlayerCreationService {

    void createPlayer(NewPlayerDTO newPlayerDTO);
}