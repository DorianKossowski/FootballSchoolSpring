package com.football_school_spring.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TeamCoachKey implements Serializable {
    private static final long serialVersionUID = 324467958454751275L;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "coach_id")
    private Long coachId;

    @Column(name = "coach_privilege_id")
    private Long coachPrivilegeId;

    public TeamCoachKey() {
    }

    public TeamCoachKey(Long teamId, Long coachId, Long coachPrivilegeId) {
        this.teamId = teamId;
        this.coachId = coachId;
        this.coachPrivilegeId = coachPrivilegeId;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getCoachPrivilegeId() {
        return coachPrivilegeId;
    }

    public void setCoachPrivilegeId(Long coachPrivilegeId) {
        this.coachPrivilegeId = coachPrivilegeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamCoachKey that = (TeamCoachKey) o;
        return Objects.equals(coachId, that.coachId) &&
                Objects.equals(teamId, that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coachId, teamId);
    }
}