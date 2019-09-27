package com.football_school_spring.services;

import com.football_school_spring.models.DTO.EditPasswordDTO;
import com.football_school_spring.models.User;

public interface AccountService {
    void updateAccount(User user);

    void changePassword(EditPasswordDTO editPasswordDTO);
}