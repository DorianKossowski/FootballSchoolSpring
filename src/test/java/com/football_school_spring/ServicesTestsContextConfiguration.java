package com.football_school_spring;

import com.football_school_spring.notifications.EmailService;
import com.football_school_spring.services.InitialAdminLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ServicesTestsContextConfiguration {

    @Bean
    public InitialAdminLoader initialAdminLoader() {
        return mock(InitialAdminLoader.class);
    }

    @Bean
    public EmailService emailService() {
        return mock(EmailService.class);
    }
}