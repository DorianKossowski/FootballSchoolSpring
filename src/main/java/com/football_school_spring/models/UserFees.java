package com.football_school_spring.models;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserFees {
    private User user;
    private Map<Integer, Boolean> fees;

    public UserFees() {
        fees = IntStream.rangeClosed(1, 12).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> false));
    }

    public UserFees(User user, Map<Integer, Boolean> paidMonths) {
        this.user = user;
        fees = IntStream.rangeClosed(1, 12).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> false));
        fees.putAll(paidMonths);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<Integer, Boolean> getFees() {
        return fees;
    }

    public void setFees(Map<Integer, Boolean> fees) {
        this.fees = fees;
    }
}