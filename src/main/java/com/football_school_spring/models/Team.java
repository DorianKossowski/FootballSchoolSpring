package com.football_school_spring.models;

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

    @OneToMany(mappedBy = "team")
    private Set<TeamCoach> teamCoaches;

    public Team() {
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
}