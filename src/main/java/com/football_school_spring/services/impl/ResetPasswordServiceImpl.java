package com.football_school_spring.services.impl;

import com.football_school_spring.models.User;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void setResetPasswordToken(String userMail, String token) {
        User user = userRepository.findByMail(userMail)
                .orElseThrow(() -> new IllegalArgumentException(String.format("There isn't user in database with mail %s", userMail)));

        user.setResetToken(token);
        userRepository.save(user);
    }

    @Override
    public void setNewPassword(String token, String password, String repPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException(String.format("There isn't user in database with token %s", token)));

        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean isResetTokenExists(String token) {
        return userRepository.findByResetToken(token).isPresent();
    }
}