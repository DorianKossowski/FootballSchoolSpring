package com.football_school_spring.models.dto;

import com.football_school_spring.models.Team;

public class CurrentTeamDTO {
    private long id;
    private String name;
    private String address;

    public CurrentTeamDTO() {
    }

    public CurrentTeamDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.address = team.getAddress();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}