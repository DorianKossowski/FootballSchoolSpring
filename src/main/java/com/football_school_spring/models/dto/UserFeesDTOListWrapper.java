package com.football_school_spring.models.dto;


import java.util.List;

public class UserFeesDTOListWrapper {
    private List<UserFeesDTO> feesList;

    public List<UserFeesDTO> getFeesList() {
        return feesList;
    }

    public void setFeesList(List<UserFeesDTO> feesList) {
        this.feesList = feesList;
    }
}