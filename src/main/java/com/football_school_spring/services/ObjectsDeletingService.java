package com.football_school_spring.services;

public interface ObjectsDeletingService {
    void deleteTeam(long id);

    void deletePlayer(long id);

    void deleteCoach(long id);

    void deleteParent(long id);
}