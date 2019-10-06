package com.football_school_spring.models.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class FixtureDTO {
    private String opponent;
    private String address;
    private LocalDate date;
    private LocalTime time;

    public FixtureDTO() {
    }

    public FixtureDTO(String address) {
        this.address = address;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}