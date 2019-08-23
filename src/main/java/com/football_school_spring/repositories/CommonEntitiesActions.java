package com.football_school_spring.repositories;

public interface CommonEntitiesActions<T> {
    boolean existsByName(String name);

    T getByName(String name);
}