package com.football_school_spring.repositories;

import com.football_school_spring.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUserMail(String mail);

    Optional<VerificationToken> findByUserId(long id);
}