package com.football_school_spring.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VerificationToken {
    private static final int EXPIRATION = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    private String token;

    private LocalDateTime expiryDate;

    public void setDefaultExpiryDate() {
        setExpiryDate(LocalDateTime.now().plusDays(EXPIRATION));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}