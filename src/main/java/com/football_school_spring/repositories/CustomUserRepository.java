package com.football_school_spring.repositories;

import com.football_school_spring.models.User;

public interface CustomUserRepository {
    void saveCompleteUser(User user);
}