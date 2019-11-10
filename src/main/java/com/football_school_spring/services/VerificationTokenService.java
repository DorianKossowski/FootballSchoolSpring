package com.football_school_spring.services;

public interface VerificationTokenService {
    void cleanUpUsersWithExpiredTokens();
}