package com.football_school_spring.utils.validation;

public class FeesAnalysisResult {
    private boolean shouldBeBlocked;
    private boolean shouldRemind;

    public boolean shouldBeBlocked() {
        return shouldBeBlocked;
    }

    public void setShouldBeBlocked(boolean shouldBeBlocked) {
        this.shouldBeBlocked = shouldBeBlocked;
    }

    public boolean shouldRemind() {
        return shouldRemind;
    }

    public void setShouldRemind(boolean shouldRemind) {
        this.shouldRemind = shouldRemind;
    }
}