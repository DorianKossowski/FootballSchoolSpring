package com.football_school_spring.services.impl;

import com.football_school_spring.models.Parent;
import com.football_school_spring.models.Player;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.NewPlayerDTO;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.ParentCreationService;
import com.football_school_spring.services.PlayerCreationService;
import com.football_school_spring.services.PlayerManageService;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class PlayerManageServiceImplTest extends ServicesTests {
    @Autowired
    private PlayerManageService underTest;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ParentCreationService parentCreationService;
    @Autowired
    private PlayerCreationService playerCreationService;

    private String parentMail = "parent@mail.com";
    private String playerName = "PlayerName";
    private Team team = new Team();
    private Parent parent = new Parent(parentMail);

    @BeforeEach
    void setUp() {
        teamRepository.save(team);
        parentCreationService.createParent(parent);

        NewPlayerDTO newPlayerDTO = new NewPlayerDTO(1L, parentMail);
        newPlayerDTO.setName(playerName);
        playerCreationService.createPlayer(newPlayerDTO);
    }

    @Test
    void shouldGetPlayerByParent() {
        Player player = underTest.getPlayerByIdAndParentId(1L, 1L);

        assertEquals(parentMail, player.getParent().getMail());
        assertEquals(playerName, player.getName());
    }

    @Test
    void shouldThrowDuringGettingPlayerByParent() {
        assertThrows(GettingFromDbException.class, () -> underTest.getPlayerByIdAndParentId(1L, 2L));
    }

    @Test
    void shouldGetPlayerByTeam() {
        Player player = underTest.getPlayerByIdAndTeamId(1L, 1L);

        assertEquals(parentMail, player.getParent().getMail());
        assertEquals(playerName, player.getName());
    }

    @Test
    void shouldUpdatePlayerWithExistingParent() {
        // Given
        String newName = "NewName";

        NewPlayerDTO newPlayerDTO = new NewPlayerDTO();
        newPlayerDTO.setName(newName);

        Player player = new Player(newPlayerDTO, parent, team);
        player.setId(1L);

        // When
        underTest.update(player);

        // Then
        Player updatedPlayer = playerRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(parentMail, updatedPlayer.getParent().getMail());
        assertEquals(newName, updatedPlayer.getName());
    }

    @Test
    void shouldUpdatePlayerWithNewExistingParent() {
        // Given
        String newName = "NewName";
        String newParentMail = "newParent@mail.com";

        NewPlayerDTO newPlayerDTO = new NewPlayerDTO();
        newPlayerDTO.setName(newName);
        Parent newParent = new Parent(newParentMail);
        parentCreationService.createParent(newParent);

        Player player = new Player(newPlayerDTO, newParent, team);
        player.setId(1L);

        // When
        underTest.update(player);

        // Then
        Player updatedPlayer = playerRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(newParentMail, updatedPlayer.getParent().getMail());
        assertEquals(2L, updatedPlayer.getParent().getId());
        assertEquals(newName, updatedPlayer.getName());
    }

    @Test
    void shouldUpdatePlayerWithNewNotExistingParent() {
        // Given
        String newName = "NewName";
        String newParentMail = "newParent@mail.com";

        NewPlayerDTO newPlayerDTO = new NewPlayerDTO();
        newPlayerDTO.setName(newName);

        Player player = new Player(newPlayerDTO, new Parent(newParentMail), team);
        player.setId(1L);

        // When
        underTest.update(player);

        // Then
        Player updatedPlayer = playerRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(newParentMail, updatedPlayer.getParent().getMail());
        assertEquals(2L, updatedPlayer.getParent().getId());
        assertEquals(newName, updatedPlayer.getName());
    }

    @Test
    void shouldDeletePlayer() {
        underTest.delete(1L);

        assertTrue(playerRepository.findAll().isEmpty());
    }
}