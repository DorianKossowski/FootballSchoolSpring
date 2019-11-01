package com.football_school_spring.services.impl;

import com.football_school_spring.WithoutInitAdminContextConfiguration;
import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.EditPasswordDTO;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.AccountService;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@Import(WithoutInitAdminContextConfiguration.class)
class AccountServiceImplTest extends ServicesTests {
    @Autowired
    private AccountService underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.saveCompleteUser(
                new User("mail@mail.com", "name", "surname", "Passw0rd", "111222333")
        );
    }

    @Test
    void shouldUpdateAccount() {
        // Given
        User user = new User();
        user.setId(1);
        user.setName("newName");
        user.setSurname("surname");
        user.setPhone("222333444");

        // When
        underTest.updateAccount(user);

        // Then
        User updatedUser = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(1, userRepository.findAll().size());
        assertEquals("newName", updatedUser.getName());
        assertEquals("surname", updatedUser.getSurname());
        assertEquals("222333444", updatedUser.getPhone());
    }

    @Test
    void shouldThrowDuringUpdatingAccount() {
        User user = new User();
        user.setId(2);

        assertThrows(GettingFromDbException.class, () -> underTest.updateAccount(user));
    }

    @Test
    void shouldChangePassword() {
        // Given
        EditPasswordDTO newPasswordDTO = new EditPasswordDTO(1L, "Passw0rd", "Passw0rd2", "Passw0rd2");

        // When
        underTest.changePassword(newPasswordDTO);

        // Then
        User updatedUser = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertTrue(bCryptPasswordEncoder.matches("Passw0rd2", updatedUser.getPassword()));
    }

    @Test
    void shouldThrowWhenWrongNewPassword() {
        EditPasswordDTO newPasswordDTO = new EditPasswordDTO(1L, "Passw0rd", "Passw0rd2", "Passw0rd3");
        assertThrows(IllegalArgumentException.class, () -> underTest.changePassword(newPasswordDTO));
    }

    @Test
    void shouldThrowWhenWrongOldPassword() {
        EditPasswordDTO newPasswordDTO = new EditPasswordDTO(1L, "Pass", "Passw0rd2", "Passw0rd2");
        assertThrows(IllegalArgumentException.class, () -> underTest.changePassword(newPasswordDTO));
    }
}