package com.football_school_spring;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FootballSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballSchoolApplication.class, args);

        // Logger configuration
        BasicConfigurator.configure();
    }
}
