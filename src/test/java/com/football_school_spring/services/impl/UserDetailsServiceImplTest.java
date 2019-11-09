package com.football_school_spring.services.impl;

import com.football_school_spring.FootballSchoolApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDetailsServiceImplTest extends FootballSchoolApplicationTests {
    @Autowired
    private UserDetailsService underTest;

    @Test
    void shouldFindUser() {
        UserDetails user = underTest.loadUserByUsername("admin@admin.com");

        assertTrue(user.isEnabled());
    }

    @Test
    void shouldThrowDuringFindingUser() {
        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername("admin2@admin.com"));
    }
}