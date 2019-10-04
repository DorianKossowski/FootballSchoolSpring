package com.football_school_spring.services;

import com.football_school_spring.models.dto.UserFeesDTO;
import com.football_school_spring.models.dto.UserFeesDTOListWrapper;

import java.util.List;

public interface FeesService {
    List<UserFeesDTO> getCoachesFees(int year);

    void setUpdatedFees(int year, UserFeesDTOListWrapper wrapper);
}