package com.football_school_spring.configuration;

import com.football_school_spring.services.InitialDataLoaderService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class WithoutInitContextConfiguration {

    @Bean
    public InitialDataLoaderService initialDataLoaderService() {
        return mock(InitialDataLoaderService.class);
    }
}