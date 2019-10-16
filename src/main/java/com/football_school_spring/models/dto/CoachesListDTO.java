package com.football_school_spring.models.dto;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.enums.CoachPrivilegeName;

public class CoachesListDTO {
    private long id;
    private String mail;
    private String name;
    private String managerInfo;
    private String status;

    public CoachesListDTO(Coach coach) {
        this.id = coach.getId();
        this.mail = coach.getMail();
        this.name = getInternalName(coach);
        this.managerInfo = getInternalManagerInfo(coach);
        this.status = coach.getUserStatus().getName();
    }

    private String getInternalName(Coach coach) {
        return coach.getName() != null ? coach.getName() + " " + coach.getSurname() : "-";
    }

    private String getInternalManagerInfo(Coach coach) {
        return coach.getMaxNumberOfTeams() > 0 ? coach.getTeamCoaches()
                .stream()
                .filter(teamCoach -> teamCoach.getCoachPrivilege().getName().equals(CoachPrivilegeName.MANAGER.getName()))
                .count() + "/" + coach.getMaxNumberOfTeams()
                : "-";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerInfo() {
        return managerInfo;
    }

    public void setManagerInfo(String managerInfo) {
        this.managerInfo = managerInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}