package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Fee;
import com.football_school_spring.models.UserFees;
import com.football_school_spring.models.UserFeesListWrapper;
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
    public List<UserFees> getCoachesFees(int year) {
        List<Coach> coaches = coachRepository.findAll();
        List<Fee> allFees = feeRepository.findAll();

        List<UserFees> usersFees = new ArrayList<>();
        coaches = coaches.stream()
                .filter(coach -> coach.isEnabled() || !coach.isAccountNonLocked())
                .collect(Collectors.toList());
        for (Coach coach : coaches) {
            Map<Integer, Boolean> paidMonths = allFees.stream()
                    .filter(fee -> fee.getDate().getYear() == year && fee.getUser().getId() == coach.getId())
                    .map(fee -> fee.getDate().getMonthValue())
                    .collect(Collectors.toMap(Function.identity(), i -> true));
            usersFees.add(new UserFees(coach, paidMonths));
        }
        return usersFees;
    }

    @Override
    @Transactional
    public void setUpdatedFees(int year, UserFeesListWrapper wrapper) {
        for (UserFees userFees : wrapper.getFeesList()) {
            long id = userFees.getUser().getId();
            for (Map.Entry<Integer, Boolean> entry : userFees.getFees().entrySet()) {
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