package com.football_school_spring.utils.validation;

public enum EditingValidationErrorName {
    NOT_EXISTS("notExists"), NOT_ACTIVE("notActive"), WRONG_NUMBER("wrongNumber");

    private String name;

    EditingValidationErrorName(String name) {
        this.name = name;
    }

    public String getUrlName() {
        return name + "=true";
    }

    public String getName() {
        return name;
    }
}