package com.football_school_spring.services.impl;

import com.football_school_spring.models.User;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.ResetPasswordService;
import com.football_school_spring.services.ServicesTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResetPasswordServiceImplTest extends ServicesTests {
    @Autowired
    private ResetPasswordService underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private String mail = "user@mail.com";
    private String token = "token";

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setMail(mail);
        user.setResetToken(token);
        userRepository.save(user);
    }

    @Test
    void shouldSetToken() {
        // Given
        String newToken = "newToken";

        // When
        underTest.setResetPasswordToken(mail, newToken);

        // Then
        assertEquals(newToken, userRepository.findById(1L).orElseThrow(RuntimeException::new).getResetToken());
    }

    @Test
    void shouldSetNewPassword() {
        // Given
        String newPassword = "newToken";

        // When
        underTest.setNewPassword(token, newPassword);

        // Then
        assertTrue(passwordEncoder.matches(newPassword, userRepository.findById(1L).orElseThrow(RuntimeException::new).getPassword()));
    }

    @Test
    void shouldTokenExists() {
        boolean resetTokenExists = underTest.isResetTokenExists(token);

        assertTrue(resetTokenExists);
    }
}