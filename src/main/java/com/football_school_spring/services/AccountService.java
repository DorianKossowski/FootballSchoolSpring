package com.football_school_spring.services;

import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.EditPasswordDTO;

public interface AccountService {
    void updateAccount(User user);

    void changePassword(EditPasswordDTO editPasswordDTO);
}