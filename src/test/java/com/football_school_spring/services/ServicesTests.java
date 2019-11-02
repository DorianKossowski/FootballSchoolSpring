package com.football_school_spring.services;

import com.football_school_spring.FootballSchoolApplicationTests;
import com.football_school_spring.ServicesTestsContextConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(ServicesTestsContextConfiguration.class)
public class ServicesTests extends FootballSchoolApplicationTests {
}