package com.football_school_spring.services;

public interface ResetPasswordService {
    void setResetPasswordToken(String userMail, String token);

    void setNewPassword(String token, String password, String repPassword);

    boolean isResetTokenExists(String token);
}