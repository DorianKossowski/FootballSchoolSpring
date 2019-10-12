package com.football_school_spring.models;

import com.football_school_spring.models.dto.NewPlayerDTO;

import javax.persistence.*;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    private User parent;

    @ManyToOne
    @JoinColumn(name = "teamId", referencedColumnName = "id")
    private Team team;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private int year;

    public Player() {
    }

    public Player(NewPlayerDTO newPlayerDTO, Parent parent, Team team) {
        this.name = newPlayerDTO.getName();
        this.surname = newPlayerDTO.getSurname();
        this.year = newPlayerDTO.getYear();
        this.parent = parent;
        this.team = team;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getParent() {
        return parent;
    }

    public void setParent(User parent) {
        this.parent = parent;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}