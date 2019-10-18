package com.football_school_spring.services.impl;

import com.football_school_spring.models.User;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.models.enums.UserTypeName;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.repositories.UserTypeRepository;
import com.football_school_spring.services.InitialAdminLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.apache.log4j.Logger.getLogger;

@Service
public class InitialAdminLoaderImpl implements InitialAdminLoader {
    private static final Logger logger = getLogger(InitialAdminLoaderImpl.class);
    @Value("${admin.mail}")
    private String mail;
    @Value("${admin.password}")
    private String password;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;

    @Override
    public void save() {
        String adminName = UserTypeName.ADMIN.getName();
        if (!userRepository.findByUserTypeName(adminName).isPresent()) {
            User admin = new User();
            admin.setMail(mail);
            admin.setPassword(password);
            admin.setName(adminName);
            admin.setUserType(userTypeRepository.getByName(adminName));
            admin.setUserStatus(userStatusRepository.getByName(UserStatusName.ACTIVE.getName()));
            admin.setDateOfCreation(LocalDateTime.now());

            userRepository.saveCompleteUser(admin);
            logger.debug("Saved initial admin");
        }
    }
}