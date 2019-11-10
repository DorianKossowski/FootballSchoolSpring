package com.football_school_spring.utils.exception;

public class ManagerWithTeamException extends RuntimeException {
    private static final long serialVersionUID = -24092238165090424L;

    public ManagerWithTeamException() {
        super("Manager has teams assigned to him");
    }
}