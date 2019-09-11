package com.football_school_spring.services;

import com.football_school_spring.models.UserRegistrationDTO;

public interface UserRegistrationService {
    void registerUser(String mail, UserRegistrationDTO userRegistrationDTO);
}