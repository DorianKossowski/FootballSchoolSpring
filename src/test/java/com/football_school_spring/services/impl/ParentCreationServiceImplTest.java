package com.football_school_spring.services.impl;

import com.football_school_spring.models.Parent;
import com.football_school_spring.models.User;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.models.enums.UserTypeName;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.ParentCreationService;
import com.football_school_spring.services.ServicesTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParentCreationServiceImplTest extends ServicesTests {
    @Autowired
    private ParentCreationService underTest;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateParent() {
        // Given
        String mail = "parent@mail.com";

        // When
        underTest.createParent(new Parent(mail));

        // Then
        assertEquals(1L, userRepository.findAll().size());

        User parent = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(mail, parent.getMail());
        assertEquals(UserTypeName.PARENT.getName(), parent.getUserType().getName());
        assertEquals(UserStatusName.WAITING_FOR_APPROVAL.getName(), parent.getUserStatus().getName());
    }
}