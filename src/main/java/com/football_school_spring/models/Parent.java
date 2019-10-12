package com.football_school_spring.models;

import javax.persistence.Entity;

@Entity
public class Parent extends User {
    private static final long serialVersionUID = 4555279041584496593L;

    public Parent() {
    }

    public Parent(String mail) {
        this.setMail(mail);
    }
}