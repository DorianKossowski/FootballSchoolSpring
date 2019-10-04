package com.football_school_spring.models.dto;

public class TeamsListDTO {
    private long id;
    private String name;
    private String managerFullName;

    public TeamsListDTO() {
    }

    public TeamsListDTO(long id, String name, String managerFullName) {
        this.id = id;
        this.name = name;
        this.managerFullName = managerFullName;
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

    public String getManagerFullName() {
        return managerFullName;
    }

    public void setManagerFullName(String managerFullName) {
        this.managerFullName = managerFullName;
    }
}