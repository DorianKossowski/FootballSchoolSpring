package com.football_school_spring.services.impl;

import com.football_school_spring.FootballSchoolApplicationTests;
import com.football_school_spring.configuration.WithoutInitContextConfiguration;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.InitialAdminLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(WithoutInitContextConfiguration.class)
class InitialAdminLoaderImplTest extends FootballSchoolApplicationTests {
    @Autowired
    private InitialAdminLoader underTest;
    @Autowired
    private UserRepository userRepository;

    @Value("${admin.mail}")
    private String mail;

    @Test
    void shouldSaveAdmin() {
        underTest.save();

        assertEquals(1L, userRepository.findAll().size());
        assertEquals(mail, userRepository.findById(1L).orElseThrow(RuntimeException::new).getMail());
    }
}