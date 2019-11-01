package com.football_school_spring.services.impl;

import com.football_school_spring.WithoutInitAdminContextConfiguration;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.ChatMessageDTO;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.ChatMessageService;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(WithoutInitAdminContextConfiguration.class)
class ChatMessageServiceImplTest extends ServicesTests {
    @Autowired
    private ChatMessageService underTest;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        teamRepository.save(new Team());
        userRepository.save(new User());
    }

    @Test
    void shouldAddMessages() {
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        underTest.addMessage("Message 1", 1L, user);
        underTest.addMessage("Message 2", 1L, user);
        assertEquals(2, underTest.getMessagesDTO(1L, user).size());
    }

    @Test
    void shouldThrowDuringAddingMessage() {
        assertThrows(GettingFromDbException.class, () ->
                underTest.addMessage("msg", 2L, userRepository.findById(1L).orElseThrow(RuntimeException::new)));
    }

    @Test
    void shouldGetMessagesInCorrectOrder() throws InterruptedException {
        // Given
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        // need to use Thread.sleep because ordering is based on time of adding
        underTest.addMessage("Message 1", 1L, user);
        Thread.sleep(1000);
        underTest.addMessage("Message 2", 1L, user);
        Thread.sleep(1000);
        underTest.addMessage("Message 3", 1L, user);

        // When
        Iterator<ChatMessageDTO> messageDTOIterator = underTest.getMessagesDTO(1L, user).iterator();

        // Then
        assertEquals("Message 3", messageDTOIterator.next().getText());
        assertEquals("Message 2", messageDTOIterator.next().getText());
        assertEquals("Message 1", messageDTOIterator.next().getText());
    }
}