package com.football_school_spring.models;

import com.football_school_spring.models.enums.CoachPrivilegeName;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String address;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private Set<TeamCoach> teamCoaches;

    public Team() {
    }

    public Team(long id) {
        this.id = id;
    }

    public Team(String name, String address) {
        this.name = name;
        this.address = address;
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

    public Set<TeamCoach> getTeamCoaches() {
        return teamCoaches;
    }

    public void setTeamCoaches(Set<TeamCoach> teamCoaches) {
        this.teamCoaches = teamCoaches;
    }

    public Coach getManager() {
        return teamCoaches.stream()
                .filter(teamCoach -> teamCoach.getCoachPrivilege().getName().equals(CoachPrivilegeName.MANAGER.getName()))
                .map(TeamCoach::getCoach)
                .findFirst().orElseThrow(() -> new RuntimeException("Team doesn't have manager"));
    }
}