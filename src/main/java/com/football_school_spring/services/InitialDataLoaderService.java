package com.football_school_spring.services;

public interface InitialDataLoaderService {
    void saveUserTypeIfNotExists(String userTypeName);

    void saveUserStatusIfNotExists(String userStatusName);

    void saveCoachPrivilegeIfNotExists(String coachPrivilegeName);

    void saveInitAdmin();
}