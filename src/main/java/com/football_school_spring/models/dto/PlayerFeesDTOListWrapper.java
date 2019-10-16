package com.football_school_spring.models.dto;


import java.util.List;

public class PlayerFeesDTOListWrapper {
    private List<PlayerFeesDTO> feesList;

    public List<PlayerFeesDTO> getFeesList() {
        return feesList;
    }

    public void setFeesList(List<PlayerFeesDTO> feesList) {
        this.feesList = feesList;
    }
}