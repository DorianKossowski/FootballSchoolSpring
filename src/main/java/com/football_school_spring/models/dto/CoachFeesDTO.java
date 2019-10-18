package com.football_school_spring.models.dto;

import com.football_school_spring.models.User;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CoachFeesDTO {
    private User user;
    private Map<Integer, Boolean> fees;

    public CoachFeesDTO() {
        fees = IntStream.rangeClosed(1, 12).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> false));
    }

    public CoachFeesDTO(User user, Map<Integer, Boolean> paidMonths, int year) {
        this.user = user;
        fees = IntStream.rangeClosed(1, 12).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> false));
        fees.forEach((month, value) -> {
            if (year == user.getDateOfCreation().getYear() && month < user.getDateOfCreation().getMonthValue()) {
                paidMonths.put(month, true);
            }
        });
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