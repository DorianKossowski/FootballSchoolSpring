package com.football_school_spring.models.enums;

public enum UserStatusName {
    WAITING_FOR_APPROVAL("waiting for approval"), BLOCKED("blocked"), ACTIVE("active");

    private String name;

    UserStatusName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}