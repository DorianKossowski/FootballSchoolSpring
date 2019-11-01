package com.football_school_spring;

import com.football_school_spring.services.InitialAdminLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class WithoutInitAdminContextConfiguration {

    @Bean
    public InitialAdminLoader initialAdminLoader() {
        return mock(InitialAdminLoader.class);
    }
}