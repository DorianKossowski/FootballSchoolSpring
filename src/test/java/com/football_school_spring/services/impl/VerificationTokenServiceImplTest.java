package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.VerificationToken;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.repositories.VerificationTokenRepository;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.services.VerificationTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VerificationTokenServiceImplTest extends ServicesTests {
    @Autowired
    private VerificationTokenService underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Test
    void shouldRegisterUser() {
        // Given
        Coach user1 = new Coach();
        user1.setId(1L);
        VerificationToken verificationToken1 = new VerificationToken();
        verificationToken1.setUser(user1);
        verificationToken1.setExpiryDate(LocalDateTime.now().minusDays(1L));

        Coach user2 = new Coach();
        user2.setId(2L);
        VerificationToken verificationToken2 = new VerificationToken();
        verificationToken2.setUser(user2);
        verificationToken2.setExpiryDate(LocalDateTime.now().plusDays(1L));

        coachRepository.saveAll(Arrays.asList(user1, user2));
        verificationTokenRepository.saveAll(Arrays.asList(verificationToken1, verificationToken2));

        // When
        underTest.cleanUpUsersWithExpiredTokens();

        // Then
        assertEquals(1, userRepository.findAll().size());
        assertEquals(1, verificationTokenRepository.findAll().size());
    }
}