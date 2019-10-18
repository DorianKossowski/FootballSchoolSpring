package com.football_school_spring.utils.validation;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.CoachFee;
import com.football_school_spring.models.PlayerFee;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class FeesAnalyser {

    public static FeesAnalysisResult analyseCoachFees(Coach coach, List<CoachFee> coachFees) {
        FeesAnalysisResult result = new FeesAnalysisResult();
        result.setShouldBeBlocked(isBlockValid(coach, coachFees));
        result.setShouldRemind(isCoachRemindValid(coachFees));
        return result;
    }

    public static FeesAnalysisResult analysePlayerFees(List<PlayerFee> playerFees) {
        FeesAnalysisResult result = new FeesAnalysisResult();
        result.setShouldRemind(isPlayerRemindValid(playerFees));
        return result;
    }

    private static boolean isCoachRemindValid(List<CoachFee> coachFees) {
        LocalDateTime now = LocalDateTime.now();
        Optional<CoachFee> feeOptional = coachFees.stream().filter(coachFee -> coachFee.getDate().getYear() == now.getYear()
                && coachFee.getDate().getMonthValue() == now.getMonth().getValue()).findAny();
        int dayOfReminder = 10;
        return !feeOptional.isPresent() && now.getDayOfMonth() == dayOfReminder;
    }

    private static boolean isPlayerRemindValid(List<PlayerFee> playerFees) {
        LocalDateTime now = LocalDateTime.now();
        Optional<PlayerFee> feeOptional = playerFees.stream().filter(playerFee -> playerFee.getDate().getYear() == now.getYear()
                && playerFee.getDate().getMonthValue() == now.getMonth().getValue()).findAny();
        int dayOfReminder = 10;
        return !feeOptional.isPresent() && now.getDayOfMonth() == dayOfReminder;
    }

    private static boolean isBlockValid(Coach coach, List<CoachFee> coachFees) {
        LocalDateTime now = LocalDateTime.now().withDayOfMonth(1);
        Optional<CoachFee> feeOptional = coachFees.stream().filter(coachFee -> coachFee.getDate().getYear() == now.getYear()
                && coachFee.getDate().getMonthValue() == now.getMonth().minus(1).getValue()).findAny();
        return !feeOptional.isPresent() &&
                coach.getDateOfCreation().truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1).isBefore(now.truncatedTo(ChronoUnit.DAYS));
    }
}