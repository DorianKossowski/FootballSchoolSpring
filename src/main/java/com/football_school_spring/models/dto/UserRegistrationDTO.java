package com.football_school_spring.models.dto;

public class UserRegistrationDTO {
    private String name;
    private String surname;
    private String phone;
    private String password;
    private String confirmPassword;

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String name, String surname, String phone, String password, String confirmPassword) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.password = password;
        this.confirmPassword = confirmPassword;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}