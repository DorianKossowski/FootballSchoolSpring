package com.football_school_spring.controllers.admin;


import java.util.List;

public class UserFeesListWrapper {
    private List<UserFees> feesList;

    public List<UserFees> getFeesList() {
        return feesList;
    }

    public void setFeesList(List<UserFees> feesList) {
        this.feesList = feesList;
    }
}