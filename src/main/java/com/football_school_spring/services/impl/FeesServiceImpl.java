package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.CoachFee;
import com.football_school_spring.models.Player;
import com.football_school_spring.models.PlayerFee;
import com.football_school_spring.models.dto.CoachFeesDTO;
import com.football_school_spring.models.dto.CoachFeesDTOListWrapper;
import com.football_school_spring.models.dto.PlayerFeesDTO;
import com.football_school_spring.models.dto.PlayerFeesDTOListWrapper;
import com.football_school_spring.repositories.CoachFeeRepository;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.PlayerFeeRepository;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.services.FeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FeesServiceImpl implements FeesService {
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private CoachFeeRepository coachFeeRepository;
    @Autowired
    private PlayerFeeRepository playerFeeRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public List<CoachFeesDTO> getCoachesFees(int year) {
        List<Coach> coaches = coachRepository.findAll();
        List<CoachFee> allCoachFees = coachFeeRepository.findAll();

        List<CoachFeesDTO> usersFees = new ArrayList<>();
        coaches = coaches.stream()
                .filter(coach -> coach.getMaxNumberOfTeams() > 0 && (coach.isEnabled() || !coach.isAccountNonLocked()))
                .collect(Collectors.toList());
        for (Coach coach : coaches) {
            Map<Integer, Boolean> paidMonths = allCoachFees.stream()
                    .filter(fee -> fee.getDate().getYear() == year && fee.getUser().getId() == coach.getId())
                    .map(fee -> fee.getDate().getMonthValue())
                    .collect(Collectors.toMap(Function.identity(), i -> true));
            usersFees.add(new CoachFeesDTO(coach, paidMonths));
        }
        return usersFees;
    }

    @Override
    public List<PlayerFeesDTO> getPlayersFees(long teamId, int year) {
        List<Player> players = playerRepository.findByTeamId(teamId);
        List<PlayerFee> teamAllFees = playerFeeRepository.findByPlayerTeamId(teamId);
        List<PlayerFeesDTO> usersFees = new ArrayList<>();

        for (Player player : players) {
            Map<Integer, Boolean> paidMonths = teamAllFees.stream()
                    .filter(fee -> fee.getDate().getYear() == year && fee.getPlayer().getId() == player.getId())
                    .map(fee -> fee.getDate().getMonthValue())
                    .collect(Collectors.toMap(Function.identity(), i -> true));
            usersFees.add(new PlayerFeesDTO(player, paidMonths));
        }
        return usersFees;
    }

    @Override
    @Transactional
    public void setUpdatedCoachesFees(int year, CoachFeesDTOListWrapper wrapper) {
        for (CoachFeesDTO coachFeesDTO : wrapper.getFeesList()) {
            long id = coachFeesDTO.getUser().getId();
            for (Map.Entry<Integer, Boolean> entry : coachFeesDTO.getFees().entrySet()) {
                LocalDate feeDate = LocalDate.of(year, entry.getKey(), 2);
                Optional<CoachFee> feeOptional = coachFeeRepository.findByUserIdAndDate(id, feeDate);
                if (entry.getValue()) {
                    if (!feeOptional.isPresent()) {
                        coachFeeRepository.save(new CoachFee(coachRepository.getOne(id), feeDate));
                    }
                } else {
                    feeOptional.ifPresent(fee -> coachFeeRepository.delete(fee));
                }
            }
        }
    }

    @Override
    @Transactional
    public void setUpdatedPlayersFees(int year, PlayerFeesDTOListWrapper wrapper) {
        for (PlayerFeesDTO playerFeesDTO : wrapper.getFeesList()) {
            long id = playerFeesDTO.getPlayer().getId();
            for (Map.Entry<Integer, Boolean> entry : playerFeesDTO.getFees().entrySet()) {
                LocalDate feeDate = LocalDate.of(year, entry.getKey(), 2);
                Optional<PlayerFee> feeOptional = playerFeeRepository.findByPlayerIdAndDate(id, feeDate);
                if (entry.getValue()) {
                    if (!feeOptional.isPresent()) {
                        playerFeeRepository.save(new PlayerFee(playerRepository.getOne(id), feeDate));
                    }
                } else {
                    feeOptional.ifPresent(fee -> playerFeeRepository.delete(fee));
                }
            }
        }
    }
}