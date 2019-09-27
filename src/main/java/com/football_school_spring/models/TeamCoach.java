package com.football_school_spring.models;

import javax.persistence.*;

@Entity
public class TeamCoach {
    @EmbeddedId
    private TeamCoachKey id;

    @ManyToOne
    @MapsId("coach_id")
    @JoinColumn(name = "coach_id")
    private Coach coach;

    @ManyToOne
    @MapsId("team_id")
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @MapsId("coach_privilege_id")
    @JoinColumn(name = "coach_privilege_id")
    private CoachPrivilege coachPrivilege;

    public TeamCoach() {
    }

    public TeamCoach(TeamCoachKey teamCoachKey) {
        this.id = teamCoachKey;
    }

    public TeamCoachKey getId() {
        return id;
    }

    public void setId(TeamCoachKey id) {
        this.id = id;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public CoachPrivilege getCoachPrivilege() {
        return coachPrivilege;
    }

    public void setCoachPrivilege(CoachPrivilege coachPrivilege) {
        this.coachPrivilege = coachPrivilege;
    }
}