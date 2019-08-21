package com.football_school_spring.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Coach extends User {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coachPrivilegeId", referencedColumnName = "id")
    private CoachPrivilege coachPrivilege;

    public CoachPrivilege getCoachPrivilege() {
        return coachPrivilege;
    }

    public void setCoachPrivilege(CoachPrivilege coachPrivilege) {
        this.coachPrivilege = coachPrivilege;
    }
}