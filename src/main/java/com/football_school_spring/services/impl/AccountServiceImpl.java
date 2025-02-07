package com.football_school_spring.services.impl;

import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.EditPasswordDTO;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.services.AccountService;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.springframework.beans.factory.annotation.Autowired;
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
                .orElseThrow(() -> new GettingFromDbException(User.class, user.getId()));
        userInDB.setName(user.getName());
        userInDB.setSurname(user.getSurname());
        userInDB.setPhone(user.getPhone());
        userRepository.save(userInDB);
    }

    @Override
    public void changePassword(EditPasswordDTO editPasswordDTO) {
        if (!editPasswordDTO.getNewPassword().equals(editPasswordDTO.getRepNewPassword())) {
            throw new IllegalArgumentException("New passwords aren't equal");
        }
        User userInDB = userRepository.findById(editPasswordDTO.getId())
                .orElseThrow(() -> new GettingFromDbException(User.class, editPasswordDTO.getId()));
        if (bCryptPasswordEncoder.matches(editPasswordDTO.getOldPassword(), userInDB.getPassword())) {
            userInDB.setPassword(bCryptPasswordEncoder.encode(editPasswordDTO.getNewPassword()));
            userRepository.save(userInDB);
        } else {
            throw new IllegalArgumentException("Entered wrong current password");
        }
    }
}