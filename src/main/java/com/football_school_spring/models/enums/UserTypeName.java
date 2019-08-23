package com.football_school_spring.models.enums;

public enum UserTypeName {
    ADMIN("admin"), COACH("coach"), PARENT("parent");

    private String name;

    UserTypeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}