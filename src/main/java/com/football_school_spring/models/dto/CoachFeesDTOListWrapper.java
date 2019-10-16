package com.football_school_spring.models.dto;


import java.util.List;

public class CoachFeesDTOListWrapper {
    private List<CoachFeesDTO> feesList;

    public List<CoachFeesDTO> getFeesList() {
        return feesList;
    }

    public void setFeesList(List<CoachFeesDTO> feesList) {
        this.feesList = feesList;
    }
}