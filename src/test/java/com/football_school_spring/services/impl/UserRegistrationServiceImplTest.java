package com.football_school_spring.services.impl;

import com.football_school_spring.models.User;
import com.football_school_spring.models.VerificationToken;
import com.football_school_spring.models.dto.UserRegistrationDTO;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.repositories.VerificationTokenRepository;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.services.UserRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationServiceImplTest extends ServicesTests {
    @Autowired
    private UserRegistrationService underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void shouldRegisterUser() {
        // Given
        String mail = "user@mail.com";
        String name = "name";
        String surname = "surname";
        String phone = "111222333";
        String password = "password";
        String confirmPassword = "password";

        User user = new User();
        user.setMail(mail);
        userRepository.save(user);

        VerificationToken token = new VerificationToken();
        token.setUser(user);
        verificationTokenRepository.save(token);

        // When
        underTest.registerUser(mail, new UserRegistrationDTO(name, surname, phone, password, confirmPassword));

        // Then
        User registeredUser = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(name, registeredUser.getName());
        assertEquals(surname, registeredUser.getSurname());
        assertEquals(phone, registeredUser.getPhone());
        assertTrue(encoder.matches(password, registeredUser.getPassword()));
    }

    @Test
    void shouldThrowDuringRegistrationUserWhenLackOfUser() {
        assertThrows(IllegalArgumentException.class, () -> underTest.registerUser("mail", new UserRegistrationDTO()));
    }

    @Test
    void shouldThrowDuringRegistrationUserWhenWrongPasswords() {
        String mail = "user@mail.com";
        String password = "password";
        String confirmPassword = "NOTpassword";
        User user = new User();
        user.setMail(mail);
        userRepository.save(user);

        assertThrows(IllegalArgumentException.class, () -> underTest.registerUser("mail",
                new UserRegistrationDTO("name", "surname", "phone", password, confirmPassword)));
    }
}