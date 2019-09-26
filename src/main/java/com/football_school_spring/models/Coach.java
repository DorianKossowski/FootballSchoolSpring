package com.football_school_spring.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Coach extends User {
    private static final long serialVersionUID = 4555279041584496593L;

    @Column
    private long maxNumberOfTeams;

    @OneToMany(mappedBy = "coach", fetch = FetchType.EAGER)
    private Set<TeamCoach> teamCoaches;

    public Coach() {
    }

    public Coach(long maxNumberOfTeams) {
        this.maxNumberOfTeams = maxNumberOfTeams;
    }

    public long getMaxNumberOfTeams() {
        return maxNumberOfTeams;
    }

    public void setMaxNumberOfTeams(long maxNumberOfTeams) {
        this.maxNumberOfTeams = maxNumberOfTeams;
    }

    public Set<TeamCoach> getTeamCoaches() {
        return teamCoaches;
    }

    public void setTeamCoaches(Set<TeamCoach> teamCoaches) {
        this.teamCoaches = teamCoaches;
    }
}