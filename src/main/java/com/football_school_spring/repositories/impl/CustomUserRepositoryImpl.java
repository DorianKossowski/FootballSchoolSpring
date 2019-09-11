package com.football_school_spring.repositories.impl;

import com.football_school_spring.models.User;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.repositories.CustomUserRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CustomUserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserStatusRepository userStatusRepository;

    @Override
    public void saveCompleteUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setUserStatus(userStatusRepository.getByName(UserStatusName.ACTIVE.getName()));
        entityManager.persist(user);
    }
}