package com.football_school_spring.models;

import com.football_school_spring.models.dto.FixtureDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Fixture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "teamId", referencedColumnName = "id")
    private Team team;

    @Column
    private String opponent;

    @Column
    private String address;

    @Column
    private boolean host;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime date;

    public Fixture() {
    }

    public Fixture(Team team, FixtureDTO fixtureDTO) {
        this.team = team;
        this.opponent = fixtureDTO.getOpponent();
        this.address = fixtureDTO.getAddress();
        this.host = team.getAddress().equals(fixtureDTO.getAddress());
        this.date = LocalDateTime.of(fixtureDTO.getDate(), fixtureDTO.getTime());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}