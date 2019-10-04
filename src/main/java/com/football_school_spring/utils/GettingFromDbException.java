package com.football_school_spring.utils;

public class GettingFromDbException extends RuntimeException {
    private static final long serialVersionUID = -24092238165090424L;

    public GettingFromDbException(Class<?> classToGet) {
        super(String.format("Can't get %s from database", classToGet.getSimpleName()));
    }

    public GettingFromDbException(Class<?> classToGet, long id) {
        super(String.format("Can't get %s with id %d from database", classToGet.getSimpleName(), id));
    }
}