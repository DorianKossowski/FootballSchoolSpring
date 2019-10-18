package com.football_school_spring.models.dto;

import com.football_school_spring.models.Player;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerFeesDTO {
    private Player player;
    private Map<Integer, Boolean> fees;

    public PlayerFeesDTO() {
        fees = IntStream.rangeClosed(1, 12).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> false));
    }

    public PlayerFeesDTO(Player player, Map<Integer, Boolean> paidMonths, int year) {
        this.player = player;
        fees = IntStream.rangeClosed(1, 12).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> false));
        fees.forEach((month, value) -> {
            if (year == player.getDateOfCreation().getYear() && month < player.getDateOfCreation().getMonthValue()) {
                paidMonths.put(month, true);
            }
        });
        fees.putAll(paidMonths);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Map<Integer, Boolean> getFees() {
        return fees;
    }

    public void setFees(Map<Integer, Boolean> fees) {
        this.fees = fees;
    }
}