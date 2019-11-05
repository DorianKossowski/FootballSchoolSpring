package com.football_school_spring.services.impl;

import com.football_school_spring.models.Parent;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.NewPlayerDTO;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.ParentCreationService;
import com.football_school_spring.services.PlayerCreationService;
import com.football_school_spring.services.ServicesTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerCreationServiceImplTest extends ServicesTests {
    @Autowired
    private PlayerCreationService underTest;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ParentCreationService parentCreationService;

    @Test
    void shouldCreatePlayerWithExistingParent() {
        // Given
        String parentMail = "parent@mail.com";
        String playerName = "PlayerName";

        teamRepository.save(new Team());
        parentCreationService.createParent(new Parent(parentMail));
        NewPlayerDTO newPlayerDTO = new NewPlayerDTO(1L, parentMail);
        newPlayerDTO.setName(playerName);

        // When
        underTest.createPlayer(newPlayerDTO);

        // Then
        assertEquals(1L, userRepository.findAll().size());
        assertEquals(parentMail, userRepository.findById(1L).orElseThrow(RuntimeException::new).getMail());

        assertEquals(1L, playerRepository.findAll().size());
        assertEquals(playerName, playerRepository.findById(1L).orElseThrow(RuntimeException::new).getName());
    }

    @Test
    void shouldCreatePlayerWithNewParent() {
        // Given
        String parentMail = "parent@mail.com";
        String playerName = "PlayerName";

        teamRepository.save(new Team());
        NewPlayerDTO newPlayerDTO = new NewPlayerDTO(1L, parentMail);
        newPlayerDTO.setName(playerName);

        // When
        underTest.createPlayer(newPlayerDTO);

        // Then
        assertEquals(1L, userRepository.findAll().size());
        assertEquals(parentMail, userRepository.findById(1L).orElseThrow(RuntimeException::new).getMail());

        assertEquals(1L, playerRepository.findAll().size());
        assertEquals(playerName, playerRepository.findById(1L).orElseThrow(RuntimeException::new).getName());
    }
}