package com.football_school_spring.services.impl;

import com.football_school_spring.models.VerificationToken;
import com.football_school_spring.repositories.VerificationTokenRepository;
import com.football_school_spring.services.ObjectsDeletingService;
import com.football_school_spring.services.VerificationTokenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.log4j.Logger.getLogger;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private static final Logger logger = getLogger(VerificationTokenServiceImpl.class);

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private ObjectsDeletingService objectsDeletingService;

    @Override
    public void cleanUpUsersWithExpiredTokens() {
        List<VerificationToken> expiredTokens = verificationTokenRepository.findAll().stream()
                .filter(verificationToken -> verificationToken.getExpiryDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        expiredTokens.forEach(verificationToken -> {
            long userId = verificationToken.getUser().getId();
            objectsDeletingService.deleteExpiredUser(userId);
            logger.info(String.format("User with id %d deleted due to token expiration", userId));
        });
    }
}