package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.CoachFee;
import com.football_school_spring.models.Player;
import com.football_school_spring.models.PlayerFee;
import com.football_school_spring.notifications.EmailService;
import com.football_school_spring.repositories.CoachFeeRepository;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.PlayerFeeRepository;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.services.CoachEditingService;
import com.football_school_spring.services.ControllingFeesDatesService;
import com.football_school_spring.utils.validation.FeesAnalyser;
import com.football_school_spring.utils.validation.FeesAnalysisResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.log4j.Logger.getLogger;

@Service
public class ControllingFeesDatesServiceImpl implements ControllingFeesDatesService {
    private static final Logger logger = getLogger(ControllingFeesDatesServiceImpl.class);

    @Autowired
    private CoachFeeRepository coachFeeRepository;
    @Autowired
    private PlayerFeeRepository playerFeeRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private CoachEditingService coachEditingService;
    @Autowired
    private EmailService emailService;

    @Override
    public void controlFees() {
        List<Coach> managers = coachRepository.findAll().stream()
                .filter(coach -> coach.getMaxNumberOfTeams() > 0 && (coach.isEnabled() || !coach.isAccountNonLocked()))
                .collect(Collectors.toList());
        List<Player> players = playerRepository.findAll();
        managers.forEach(this::controlCoachFees);
        players.forEach(this::controlPlayerFees);
    }

    private void controlCoachFees(Coach coach) {
        List<CoachFee> coachFees = coachFeeRepository.findByUserId(coach.getId());
        FeesAnalysisResult result = FeesAnalyser.analyseCoachFees(coach, coachFees);
        if (result.shouldBeBlocked()) {
            coachEditingService.changeCoachStatus(coach);
            emailService.sendMailWithBlockInfo(coach.getMail());
            logger.info(String.format("User with id: %d has been blocked", coach.getId()));
        } else if (result.shouldRemind()) {
            emailService.sendMailWithFeeReminder(coach.getMail());
            logger.info(String.format("User with id: %d has been reminded about service fees", coach.getId()));
        }
    }

    private void controlPlayerFees(Player player) {
        List<PlayerFee> playerFees = playerFeeRepository.findByPlayerId(player.getId());
        FeesAnalysisResult result = FeesAnalyser.analysePlayerFees(playerFees);
        if (result.shouldRemind() && player.getParent() != null) {
            emailService.sendMailWithFeeReminder(player.getParent().getMail());
            logger.info(String.format("User with id: %d has been reminded about service fees", player.getId()));
        }
    }
}