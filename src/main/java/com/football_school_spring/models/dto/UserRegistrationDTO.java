package com.football_school_spring.models.dto;

public class UserRegistrationDTO {
    private String name;
    private String surname;
    private String phone;
    private String password;

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String name, String surname, String phone, String password) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.password = password;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}