package com.football_school_spring.utils.validation;

import com.football_school_spring.models.Coach;

public class CoachEditingValidationResult {
    private Coach coach;
    private EditingValidationErrorName errorName;
    private String errorMessage;

    private CoachEditingValidationResult(EditingValidationErrorName errorName, String errorMessage) {
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    private CoachEditingValidationResult(Coach coach) {
        this.coach = coach;
    }

    public static CoachEditingValidationResult valid(Coach coach) {
        return new CoachEditingValidationResult(coach);
    }

    public static CoachEditingValidationResult invalid(EditingValidationErrorName name, String errorMessage) {
        return new CoachEditingValidationResult(name, errorMessage);
    }

    public boolean isValid() {
        return coach != null;
    }

    public EditingValidationErrorName getErrorName() {
        return errorName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Coach getCoach() {
        return coach;
    }
}