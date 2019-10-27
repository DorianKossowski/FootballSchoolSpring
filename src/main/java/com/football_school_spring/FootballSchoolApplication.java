package com.football_school_spring;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class FootballSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballSchoolApplication.class, args);

        // Logger configuration
        BasicConfigurator.configure();
    }

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
    }
}
