package com.football_school_spring.services;

import com.football_school_spring.models.UserFees;
import com.football_school_spring.models.UserFeesListWrapper;

import java.util.List;

public interface FeesService {
    List<UserFees> getCoachesFees(int year);

    void setUpdatedFees(int year, UserFeesListWrapper wrapper);
}