package com.football_school_spring.models.enums;

public enum CoachPrivilegeName {
    MANAGER("manager"), COACH("coach");

    private String name;

    CoachPrivilegeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}