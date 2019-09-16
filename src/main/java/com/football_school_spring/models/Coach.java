package com.football_school_spring.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Coach extends User {
    private static final long serialVersionUID = 4555279041584496593L;
    @ManyToOne
    @JoinColumn(name = "coachPrivilegeId", referencedColumnName = "id")
    private CoachPrivilege coachPrivilege;

    public CoachPrivilege getCoachPrivilege() {
        return coachPrivilege;
    }

    public void setCoachPrivilege(CoachPrivilege coachPrivilege) {
        this.coachPrivilege = coachPrivilege;
    }
}