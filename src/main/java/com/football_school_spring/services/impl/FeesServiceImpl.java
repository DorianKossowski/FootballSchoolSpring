package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Fee;
import com.football_school_spring.models.dto.UserFeesDTO;
import com.football_school_spring.models.dto.UserFeesDTOListWrapper;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.FeeRepository;
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
    private FeeRepository feeRepository;

    @Override
    public List<UserFeesDTO> getCoachesFees(int year) {
        List<Coach> coaches = coachRepository.findAll();
        List<Fee> allFees = feeRepository.findAll();

        List<UserFeesDTO> usersFees = new ArrayList<>();
        coaches = coaches.stream()
                .filter(coach -> coach.isEnabled() || !coach.isAccountNonLocked())
                .collect(Collectors.toList());
        for (Coach coach : coaches) {
            Map<Integer, Boolean> paidMonths = allFees.stream()
                    .filter(fee -> fee.getDate().getYear() == year && fee.getUser().getId() == coach.getId())
                    .map(fee -> fee.getDate().getMonthValue())
                    .collect(Collectors.toMap(Function.identity(), i -> true));
            usersFees.add(new UserFeesDTO(coach, paidMonths));
        }
        return usersFees;
    }

    @Override
    @Transactional
    public void setUpdatedFees(int year, UserFeesDTOListWrapper wrapper) {
        for (UserFeesDTO userFeesDTO : wrapper.getFeesList()) {
            long id = userFeesDTO.getUser().getId();
            for (Map.Entry<Integer, Boolean> entry : userFeesDTO.getFees().entrySet()) {
                LocalDate feeDate = LocalDate.of(year, entry.getKey(), 2);
                Optional<Fee> feeOptional = feeRepository.findByUserIdAndDate(id, feeDate);
                if (entry.getValue()) {
                    if (!feeOptional.isPresent()) {
                        feeRepository.save(new Fee(coachRepository.getOne(id), feeDate));
                    }
                } else {
                    feeOptional.ifPresent(fee -> feeRepository.delete(fee));
                }
            }
        }
    }
}