package com.football_school_spring.services.impl;

import com.football_school_spring.models.EditPasswordDTO;
import com.football_school_spring.models.User;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void updateAccount(User user) {
        User userInDB = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("There isn't user in database with id %d", user.getId())));
        userInDB.setName(user.getName());
        userInDB.setSurname(user.getSurname());
        userInDB.setPhone(user.getPhone());
        userRepository.save(userInDB);

        // unnecessary to update panel with name of currently logged user
        Authentication authentication = new UsernamePasswordAuthenticationToken(userInDB, userInDB.getPassword(), userInDB.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void changePassword(EditPasswordDTO editPasswordDTO) {
        if (!editPasswordDTO.getNewPassword().equals(editPasswordDTO.getRepNewPassword())) {
            throw new IllegalArgumentException("New passwords aren't equal");
        }
        User userInDB = userRepository.findById(editPasswordDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("There isn't user in database with id %d", editPasswordDTO.getId())));
        if (bCryptPasswordEncoder.matches(editPasswordDTO.getOldPassword(), userInDB.getPassword())) {
            userInDB.setPassword(bCryptPasswordEncoder.encode(editPasswordDTO.getNewPassword()));
            userRepository.save(userInDB);
        } else {
            throw new IllegalArgumentException("Entered wrong current password");
        }
    }
}