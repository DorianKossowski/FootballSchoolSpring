package com.football_school_spring.services;

import com.football_school_spring.models.dto.CoachFeesDTO;
import com.football_school_spring.models.dto.CoachFeesDTOListWrapper;
import com.football_school_spring.models.dto.PlayerFeesDTO;
import com.football_school_spring.models.dto.PlayerFeesDTOListWrapper;

import java.util.List;

public interface FeesService {
    List<CoachFeesDTO> getCoachesFees(int year);

    List<PlayerFeesDTO> getPlayersFees(long teamId, int year);

    void setUpdatedCoachesFees(int year, CoachFeesDTOListWrapper wrapper);

    void setUpdatedPlayersFees(int year, PlayerFeesDTOListWrapper wrapper);
}