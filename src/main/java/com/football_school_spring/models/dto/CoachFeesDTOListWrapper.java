package com.football_school_spring.models.dto;


import java.util.List;

public class CoachFeesDTOListWrapper {
    private List<CoachFeesDTO> feesList;

    public CoachFeesDTOListWrapper() {
    }

    public CoachFeesDTOListWrapper(List<CoachFeesDTO> feesList) {
        this.feesList = feesList;
    }

    public List<CoachFeesDTO> getFeesList() {
        return feesList;
    }

    public void setFeesList(List<CoachFeesDTO> feesList) {
        this.feesList = feesList;
    }
}