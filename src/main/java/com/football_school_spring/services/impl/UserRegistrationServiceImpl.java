package com.football_school_spring.services.impl;

import com.football_school_spring.models.User;
import com.football_school_spring.models.VerificationToken;
import com.football_school_spring.models.dto.UserRegistrationDTO;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.repositories.VerificationTokenRepository;
import com.football_school_spring.services.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    @Transactional
    public void registerUser(String mail, UserRegistrationDTO userRegistrationDTO) {
        Optional<User> userOptional = userRepository.findByMail(mail);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("User with mail %s doesn't exist", mail));
        }
        User newUser = userOptional.get();
        setRegistrationAttributes(userRegistrationDTO, newUser);
        userRepository.saveCompleteUser(newUser);

        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByUserMail(mail);
        verificationTokenRepository.delete(verificationTokenOptional.orElseThrow(() -> new IllegalArgumentException("Token doesn't exist")));
    }

    private void setRegistrationAttributes(UserRegistrationDTO userRegistrationDTO, User newUser) {
        newUser.setName(userRegistrationDTO.getName());
        newUser.setSurname(userRegistrationDTO.getSurname());
        newUser.setPhone(userRegistrationDTO.getPhone());
        newUser.setPassword(userRegistrationDTO.getPassword());
    }
}